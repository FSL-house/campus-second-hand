package com.campus.service;

import com.campus.dto.OrderStatusUpdateDTO;
import com.campus.vo.OrderVO;

import java.util.List;

/**
 * 订单业务层接口。
 */
public interface OrderService {

    /**
     * 创建订单。
     *
     * @param productId 商品 ID
     */
    void createOrder(Long productId);

    /**
     * 查询我买到的订单。
     *
     * @return 订单列表
     */
    List<OrderVO> listMyBuyOrders();

    /**
     * 查询我卖出的订单。
     *
     * @return 订单列表
     */
    List<OrderVO> listMySellOrders();

    /**
     * 修改订单状态。
     *
     * @param updateDTO 修改参数
     */
    void updateStatus(OrderStatusUpdateDTO updateDTO);
}
