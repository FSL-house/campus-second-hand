package com.campus.dto;

import lombok.Data;

/**
 * 管理员修改商品状态请求参数。
 */
@Data
public class AdminProductStatusDTO {

    /**
     * 商品 ID。
     */
    private Long productId;

    /**
     * 商品状态。
     */
    private String status;
}
