package com.campus.dto;

import lombok.Data;

/**
 * 用户注册请求参数。
 */
@Data
public class UserRegisterDTO {

    /**
     * 用户名。
     */
    private String username;

    /**
     * 密码。
     */
    private String password;

    /**
     * 昵称。
     */
    private String nickname;

    /**
     * 手机号。
     */
    private String phone;
}
