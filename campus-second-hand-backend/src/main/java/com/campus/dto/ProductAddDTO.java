package com.campus.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发布商品请求参数。
 */
@Data
public class ProductAddDTO {

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
}
