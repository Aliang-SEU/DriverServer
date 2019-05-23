package cn.edu.seu.driverserver.controller;


import cn.edu.seu.driverserver.common.GoFastDfsApi;
import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.mapper.PeersMapper;
import cn.edu.seu.driverserver.service.IndexService;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {


    @Autowired
    IndexService indexService;

    /**
     * 首页
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/main")
    public String main(Model model) {
        Properties props=System.getProperties();
        model.addAttribute("osName", props.getProperty("os.name"));
        model.addAttribute("osArch", props.getProperty("os.arch"));
        return "main";
    }

    @RequestMapping("/main/getStat")
    @ResponseBody
    public Response getStat(){
        try {
            //获取文件信息,这一部分有待优化
            String string = HttpUtil.get(getPeersUrl()+ GoFastDfsApi.STAT);
            JSONObject parseObj = JSONUtil.parseObj(string);
            if(parseObj.get("status").equals("ok")) {
                Map<String, Object> result = indexService.getFileStat(parseObj.get("data"));
                return new Response(Response.RESPONSE_CODE_SUCCESS, result);
            }else{
                return new Response(Response.RESPONSE_CODE_ERROR,"调取文件服务器接口失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(Response.RESPONSE_CODE_ERROR,"系统异常");
    }
}
