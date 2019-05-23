package cn.edu.seu.driverserver.controller;

import cn.edu.seu.driverserver.domin.Peers;
import cn.edu.seu.driverserver.domin.User;
import cn.edu.seu.driverserver.mapper.PeersMapper;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {


    @Autowired
    PeersMapper peersMapper;

    /**
     * 获取已登录用户信息
     * @return User
     */
    public User getUser(){
        Subject subject = SecurityUtils.getSubject();
        User user=new User();
        BeanUtils.copyProperties(subject.getPrincipals().getPrimaryPrincipal(), user);
        return user;
    }

    /**
     * 获取当前用户使用的集群信息
     * @return Peers
     */
    public Peers getPeers(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Peers) request.getSession().getAttribute("peers");
    }

    /**
     * 获取组名
     * @return String
     */
    public String getPeersGroupName(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Peers peers = (Peers) request.getSession().getAttribute("peers");
        return peers.getGroupName();
    }

    /**
     * 获取url前缀,(地址+组名)
     * @return String
     */
    public String getPeersUrl(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Peers peers = (Peers) request.getSession().getAttribute("peers");
        if(peers == null) {
            peers = peersMapper.getAllPeers().get(0);
        }
        String peersUrl = peers.getServerAddress();
        if(StrUtil.isNotBlank(peers.getGroupName())){
            peersUrl += "/" + peers.getGroupName();
        }
        return peersUrl;
    }
}
