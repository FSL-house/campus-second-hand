package com.campus.vo;

import lombok.Data;

/**
 * 登录成功后的返回对象。
 */
@Data
public class LoginVO {

    /**
     * 登录 token。
     */
    private String token;

    /**
     * 当前登录用户信息。
     */
    private UserInfoVO userInfo;
}
