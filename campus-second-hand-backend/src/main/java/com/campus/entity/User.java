package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类。
 *
 * 对应数据库中的 user 表。
 */
@Data
@TableName("user")
public class User {

    /**
     * 用户 ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 角色。
     */
    private String role;

    /**
     * 创建时间。
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间。
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
