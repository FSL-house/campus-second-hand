package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.OrderStatusUpdateDTO;
import com.campus.service.OrderService;
import com.campus.vo.OrderVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单控制层。
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 订单业务层。
     */
    @Resource
    private OrderService orderService;

    /**
     * 创建订单接口。
     *
     * @param productId 商品 ID
     * @return 统一返回结果
     */
    @PostMapping("/create/{productId}")
    public Result<String> create(@PathVariable Long productId) {
        orderService.createOrder(productId);
        return Result.success("下单成功");
    }

    /**
     * 查询我买到的订单接口。
     *
     * @return 订单列表
     */
    @GetMapping("/my-buy")
    public Result<List<OrderVO>> myBuyOrders() {
        return Result.success(orderService.listMyBuyOrders());
    }

    /**
     * 查询我卖出的订单接口。
     *
     * @return 订单列表
     */
    @GetMapping("/my-sell")
    public Result<List<OrderVO>> mySellOrders() {
        return Result.success(orderService.listMySellOrders());
    }

    /**
     * 修改订单状态接口。
     *
     * @param updateDTO 修改参数
     * @return 统一返回结果
     */
    @PostMapping("/update-status")
    public Result<String> updateStatus(@RequestBody OrderStatusUpdateDTO updateDTO) {
        orderService.updateStatus(updateDTO);
        return Result.success("订单状态修改成功");
    }
}
