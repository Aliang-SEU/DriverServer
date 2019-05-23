package cn.edu.seu.driverserver.controller;


import cn.edu.seu.driverserver.util.RegexUtil;
import cn.edu.seu.driverserver.common.GoFastDfsApi;
import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.domin.User;
import cn.edu.seu.driverserver.service.UserService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class UserController extends BaseController{

    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * 用户登录操作
     *
     * @param user
     * @param rememberMe
     * @param session
     * @return
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public Response doLogin(User user, Boolean rememberMe, HttpSession session) {
        Response response = null;

        if (rememberMe == null) {
            rememberMe = false;
        }
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword(), rememberMe);
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            response = new Response(Response.RESPONSE_CODE_SUCCESS);
            logger.info(user.getAccount() + ">>>login");
        } catch (IncorrectCredentialsException e) {
            logger.info(user.getAccount() + e.getMessage());
            response = new Response(Response.RESPONSE_CODE_FAILURE, "密码错误");
        } catch (UnknownAccountException e) {
            logger.info(user.getAccount() + e.getMessage());
            response = new Response(Response.RESPONSE_CODE_FAILURE, "用户不存在");
        } catch (Exception e) {
            logger.error(user.getAccount() + e.getMessage());
            response = new Response(Response.RESPONSE_CODE_ERROR, "系统异常");
        }

        return response;
    }

    /**
     * 第一次登录系统设置管理员与文件服务器地址
     *
     * @param user
     * @return
     */
    @PostMapping("/doInstall")
    @ResponseBody
    public Response doInstall(@Valid User user, BindingResult bindingResult, String server) {

        Response response = null;

        if (bindingResult.hasErrors()) {
            response = new Response(Response.RESPONSE_CODE_ERROR, " 注册信息有误!");
            return response;
        }

        if (userService.isRegistered(user.getUsername())) {
            response = new Response(Response.RESPONSE_CODE_FAILURE, "您已注册过，请直接进行登录!");
            return response;
        }

        if (StrUtil.isBlank(server) || server.length() > 100) {
            response = new Response(Response.RESPONSE_CODE_ERROR, "请填写正确的服务器的地址!");
            return response;
        }
        //地址格式校验
        if (!RegexUtil.verifyServerUrl(server)) {
            response = new Response(Response.RESPONSE_CODE_ERROR, "服务器地址格式不正确，请重新进行填写!");
        }

        try {
            String urlPath = server;
            String result = HttpUtil.get(urlPath + GoFastDfsApi.STAT);
            JSONObject parseObj = JSONUtil.parseObj(result);
            if (!parseObj.get("status").equals("ok")) {
                response = new Response(Response.RESPONSE_CODE_ERROR, "连接文件服务器失败!请检查服务地址是否已配置白名单!");
                return response;
            }
        }catch (Exception e){
            response = new Response(Response.RESPONSE_CODE_ERROR, "连接文件服务失败!请检查服务地址是否正确!");
            return response;
        }

        response = userService.install(user, server);
        return response;
    }

    /**
     * 个人资料
     * @return String
     */
    @RequestMapping("/settings/user")
    public String user(Model model){
        model.addAttribute("user", userService.getUserById(getUser().getId()));
        return "settings/user";
    }

    /**
     * 修改个人资料(垃圾代码/有空再改~)
     * @return AjaxResult
     */
    @RequestMapping("/settings/editUser")
    @ResponseBody
    public Response editUser(User user,String oldPassword,String newPassword){
        if(StrUtil.isBlank(user.getAccount()) || user.getAccount().length() > 100){
            return new Response(Response.RESPONSE_CODE_ERROR,"昵称不能为空且在100字符以内");
        }
        if(StrUtil.isNotBlank(newPassword)){
            if(StrUtil.isBlank(oldPassword) || oldPassword.length() > 16){
                return new Response(Response.RESPONSE_CODE_ERROR,"请输入原密码");
            }
            if(newPassword.length() > 16 || newPassword.length() < 6){
                return new Response(Response.RESPONSE_CODE_ERROR,"新密码必须在6-16字符之间");
            }
            User userResult = userService.getUserById(user.getId());
            if(userResult.getPassword().equals(new Md5Hash(oldPassword,userResult.getCredentialsSalt()).toString())){
                user.setPassword(new Md5Hash(newPassword,userResult.getCredentialsSalt()).toString());
            }else{
                return new Response(Response.RESPONSE_CODE_ERROR,"原密码错误");
            }
        }
        if(userService.alterUserInfo(user)){
            return new Response(Response.RESPONSE_CODE_SUCCESS);
        }
        return new Response(Response.RESPONSE_CODE_ERROR,"修改失败");
    }

    /**
     * 登出
     * @return String
     */
    @RequestMapping(path="/logout",method= RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        User user=new User();
        BeanUtils.copyProperties(subject.getPrincipals().getPrimaryPrincipal(), user);
        logger.info(user.getAccount()+" >>>logout");
        subject.logout();
        return "redirect:/login";
    }

}
