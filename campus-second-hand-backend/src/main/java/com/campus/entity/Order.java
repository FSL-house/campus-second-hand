package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类。
 *
 * 对应数据库中的 orders 表。
 */
@Data
@TableName("orders")
public class Order {

    /**
     * 订单 ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号。
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 商品 ID。
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 买家 ID。
     */
    @TableField("buyer_id")
    private Long buyerId;

    /**
     * 卖家 ID。
     */
    @TableField("seller_id")
    private Long sellerId;

    /**
     * 成交价格。
     */
    private BigDecimal price;

    /**
     * 订单状态。
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
