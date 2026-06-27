package com.campus.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 返回给前端的商品信息对象。
 */
@Data
public class ProductVO {

    /**
     * 商品 ID。
     */
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
    private Long categoryId;

    /**
     * 分类名称。
     */
    private String categoryName;

    /**
     * 发布用户 ID。
     */
    private Long userId;

    /**
     * 发布人昵称。
     */
    private String sellerNickname;

    /**
     * 商品状态。
     */
    private String status;

    /**
     * 创建时间。
     */
    private LocalDateTime createTime;
}
