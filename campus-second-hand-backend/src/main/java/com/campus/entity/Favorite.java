package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类。
 *
 * 对应数据库中的 favorite 表。
 */
@Data
@TableName("favorite")
public class Favorite {

    /**
     * 收藏 ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID。
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品 ID。
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 创建时间。
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
