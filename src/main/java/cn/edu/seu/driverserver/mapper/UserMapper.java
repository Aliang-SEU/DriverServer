package cn.edu.seu.driverserver.mapper;

import cn.edu.seu.driverserver.domin.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 采用注解方式进行开发
 * @author hzl
 */
@Mapper
public interface UserMapper {

    /**
     * 根据id查询用户的信息
     * @param id
     * @return
     */
    @Select("select * from t_user where id = #{id}")
    User getUserById(Integer id);

    /**
     * 根据用户名获取对应的用户信息
     * @param username
     * @return
     */
    @Select("select * from t_user where username = #{username}")
    User getUserByUsername(String username);

    /**
     * 根据用户的名称查询对应的用户数据
     * @param username
     * @return
     */
    @Select("select count(1) from t_user where username=#{username}")
    Integer getUserCountByUsername(String username);

    /**
     * 添加一个已经注册过的用户
     * @param user
     */
    @Insert("insert into t_user(role, username, password, account, credentialsSalt, createTime, updateTime) " +
            "values(#{role}, #{username}, #{password}, #{account}, #{credentialsSalt}, #{createTime}, #{updateTime})")
    void saveUser(User user);

    /**
     * 获取已经注册过得用户
     * @return
     */
    @Select("select count(1) from t_user")
    Long getUserCount();

    /**
     * 修改用户的信息
     * @param user
     * @return
     */
    @Update("<script>UPDATE t_user SET" +
            "<if test='account != null'> account = #{account},</if>" +
            "<if test='password != null'> password = #{password},</if>" +
            " id = #{id} WHERE id=#{id}" +
            "</script>")
    long updateUser(User user);


}
