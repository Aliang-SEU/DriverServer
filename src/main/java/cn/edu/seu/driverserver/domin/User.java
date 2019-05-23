package cn.edu.seu.driverserver.domin;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer role;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String account; //账号名

    private String credentialsSalt; //密码加盐

    private String createTime;

    private String updateTime;
}
