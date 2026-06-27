package com.campus.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    /**
     * 用户 ID。
     */
    private Long userId;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 用户角色。
     */
    private String role;
}
