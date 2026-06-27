package com.campus.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 返回给前端的订单信息对象。
 */
@Data
public class OrderVO {

    /**
     * 订单 ID。
     */
    private Long id;

    /**
     * 订单编号。
     */
    private String orderNo;

    /**
     * 商品 ID。
     */
    private Long productId;

    /**
     * 商品标题。
     */
    private String productTitle;

    /**
     * 商品图片。
     */
    private String productImage;

    /**
     * 买家 ID。
     */
    private Long buyerId;

    /**
     * 买家昵称。
     */
    private String buyerNickname;

    /**
     * 卖家 ID。
     */
    private Long sellerId;

    /**
     * 卖家昵称。
     */
    private String sellerNickname;

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
    private LocalDateTime createTime;
}
