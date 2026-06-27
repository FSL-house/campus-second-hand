package com.campus.dto;

import lombok.Data;

/**
 * 用户登录请求参数。
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名。
     */
    private String username;

    /**
     * 密码。
     */
    private String password;
}
