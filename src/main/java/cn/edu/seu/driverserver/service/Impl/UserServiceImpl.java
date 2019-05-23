package cn.edu.seu.driverserver.service.Impl;

import cn.edu.seu.driverserver.util.DateUtil;
import cn.edu.seu.driverserver.util.StringUtil;
import cn.edu.seu.driverserver.domin.Peers;
import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.domin.User;
import cn.edu.seu.driverserver.mapper.PeersMapper;
import cn.edu.seu.driverserver.mapper.UserMapper;
import cn.edu.seu.driverserver.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserMapper userMapper;

    @Autowired
    PeersMapper peersMapper;


    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    /**
     * 查询账号的注册状态
     *
     * @param username
     * @return
     */
    @Override
    public Boolean isRegistered(String username) {
        int count = userMapper.getUserCountByUsername(username);
        return count > 0;
    }

    /**
     * 第一次登录之后执行的安装操作，包括管理员账号的设置，以及文件服务器地址的设置
     *
     * @param user
     * @param server
     * @return
     */
    @Override
    public Response install(User user, String server) {

        String date = DateUtil.getFormatDate(new Date());

        //存储服务器的信息
        Peers peers = new Peers();
        peers.setName("文件服务器");
        peers.setGroupName("default");
        peers.setServerAddress(server);
        peers.setCreateTime(date);
        peers.setUpdateTime(date);
        peersMapper.add(peers);

        String uuid = StringUtil.getUUID();
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), uuid); //md5加密
        user.setPassword(md5Hash.toString());
        user.setCredentialsSalt(uuid);
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setRole(1);    //设置为管理员
        userMapper.saveUser(user);
        return new Response(Response.RESPONSE_CODE_SUCCESS);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public Boolean alterUserInfo(User user) {
        return userMapper.updateUser(user) > 0;
    }
}
