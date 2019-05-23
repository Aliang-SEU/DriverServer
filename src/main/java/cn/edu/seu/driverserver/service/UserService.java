package cn.edu.seu.driverserver.service;

import cn.edu.seu.driverserver.domin.Response;
import cn.edu.seu.driverserver.domin.User;

public interface UserService {

    public User getUserById(Integer id);

    public Boolean isRegistered(String username);

    public Response install(User user, String server);

    public User getUserByUsername(String username);

    public Boolean alterUserInfo(User user);
}
