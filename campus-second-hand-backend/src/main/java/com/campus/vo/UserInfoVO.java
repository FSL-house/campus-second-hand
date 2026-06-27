package com.campus.vo;

import lombok.Data;

/**
 * 返回给前端的用户信息对象。
 */
@Data
public class UserInfoVO {

    /**
     * 用户 ID。
     */
    private Long id;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 昵称。
     */
    private String nickname;

    /**
     * 手机号。
     */
    private String phone;

    /**
     * 用户角色。
     */
    private String role;
}
