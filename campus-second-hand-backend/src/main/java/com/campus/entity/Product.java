package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类。
 *
 * 对应数据库中的 product 表。
 */
@Data
@TableName("product")
public class Product {

    /**
     * 商品 ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品标题。
     */
    private String title;

    /**
     * 商品描述。
     */
    private String description;

    /**
     * 商品价格。
     */
    private BigDecimal price;

    /**
     * 商品图片地址。
     */
    private String image;

    /**
     * 分类 ID。
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 发布用户 ID。
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品状态。
     */
    private String status;

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
