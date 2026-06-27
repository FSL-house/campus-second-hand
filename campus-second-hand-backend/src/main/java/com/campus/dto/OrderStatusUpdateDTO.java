package com.campus.dto;

import lombok.Data;

/**
 * 修改订单状态请求参数。
 */
@Data
public class OrderStatusUpdateDTO {

    /**
     * 订单 ID。
     */
    private Long orderId;

    /**
     * 目标状态。
     */
    private String status;
}
