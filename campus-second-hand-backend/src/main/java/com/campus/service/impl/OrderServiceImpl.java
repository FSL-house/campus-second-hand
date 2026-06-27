package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.dto.OrderStatusUpdateDTO;
import com.campus.entity.Order;
import com.campus.entity.Product;
import com.campus.entity.User;
import com.campus.mapper.OrderMapper;
import com.campus.mapper.ProductMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.OrderService;
import com.campus.utils.UserContext;
import com.campus.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单业务层实现类。
 */
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 订单 Mapper。
     */
    @Resource
    private OrderMapper orderMapper;

    /**
     * 商品 Mapper。
     */
    @Resource
    private ProductMapper productMapper;

    /**
     * 用户 Mapper。
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 创建订单。
     *
     * 主要逻辑：
     * 1. 校验商品是否存在且已上架
     * 2. 校验不能购买自己的商品
     * 3. 创建订单
     * 4. 下单后把商品改为已下架，防止重复购买
     *
     * @param productId 商品 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Long productId) {
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        Long buyerId = UserContext.getUserId();
        if (buyerId == null) {
            throw new RuntimeException("请先登录");
        }

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!"ON_SALE".equals(product.getStatus())) {
            throw new RuntimeException("该商品当前不可购买");
        }
        if (buyerId.equals(product.getUserId())) {
            throw new RuntimeException("不能购买自己发布的商品");
        }

        LambdaQueryWrapper<Order> orderQueryWrapper = new LambdaQueryWrapper<>();
        orderQueryWrapper.eq(Order::getProductId, productId)
                .in(Order::getStatus, "PENDING_DEAL", "FINISHED");
        Long count = orderMapper.selectCount(orderQueryWrapper);
        if (count != null && count > 0) {
            throw new RuntimeException("该商品已被下单");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setProductId(product.getId());
        order.setBuyerId(buyerId);
        order.setSellerId(product.getUserId());
        order.setPrice(product.getPrice());
        order.setStatus("PENDING_DEAL");
        orderMapper.insert(order);

        product.setStatus("OFF_SALE");
        productMapper.updateById(product);
    }

    /**
     * 查询我买到的订单。
     *
     * @return 订单列表
     */
    @Override
    public List<OrderVO> listMyBuyOrders() {
        Long buyerId = UserContext.getUserId();
        if (buyerId == null) {
            throw new RuntimeException("请先登录");
        }

        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getBuyerId, buyerId)
                .orderByDesc(Order::getCreateTime);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        return buildOrderVOList(orderList);
    }

    /**
     * 查询我卖出的订单。
     *
     * @return 订单列表
     */
    @Override
    public List<OrderVO> listMySellOrders() {
        Long sellerId = UserContext.getUserId();
        if (sellerId == null) {
            throw new RuntimeException("请先登录");
        }

        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getSellerId, sellerId)
                .orderByDesc(Order::getCreateTime);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        return buildOrderVOList(orderList);
    }

    /**
     * 修改订单状态。
     *
     * 这里先支持两种操作：
     * 1. 改成 FINISHED，表示订单完成
     * 2. 改成 CANCELLED，表示取消订单，并把商品重新上架
     *
     * @param updateDTO 修改参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(OrderStatusUpdateDTO updateDTO) {
        if (updateDTO == null) {
            throw new RuntimeException("订单参数不能为空");
        }
        if (updateDTO.getOrderId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (!StringUtils.hasText(updateDTO.getStatus())) {
            throw new RuntimeException("订单状态不能为空");
        }

        Order order = orderMapper.selectById(updateDTO.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            throw new RuntimeException("请先登录");
        }
        if (!currentUserId.equals(order.getBuyerId()) && !currentUserId.equals(order.getSellerId())) {
            throw new RuntimeException("无权操作该订单");
        }

        if (!"PENDING_DEAL".equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不能再修改");
        }

        String targetStatus = updateDTO.getStatus();
        if (!"FINISHED".equals(targetStatus) && !"CANCELLED".equals(targetStatus)) {
            throw new RuntimeException("只支持修改为已完成或已取消");
        }

        order.setStatus(targetStatus);
        orderMapper.updateById(order);

        Product product = productMapper.selectById(order.getProductId());
        if (product != null && "CANCELLED".equals(targetStatus)) {
            product.setStatus("ON_SALE");
            productMapper.updateById(product);
        }
    }

    /**
     * 把订单实体列表转换成前端使用的订单 VO 列表。
     *
     * 这里会批量查询商品和用户信息，减少数据库查询次数。
     *
     * @param orderList 订单实体列表
     * @return 订单 VO 列表
     */
    private List<OrderVO> buildOrderVOList(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> productIdSet = orderList.stream()
                .map(Order::getProductId)
                .collect(Collectors.toSet());
        Set<Long> userIdSet = orderList.stream()
                .flatMap(order -> java.util.stream.Stream.of(order.getBuyerId(), order.getSellerId()))
                .collect(Collectors.toSet());

        LambdaQueryWrapper<Product> productQueryWrapper = new LambdaQueryWrapper<>();
        productQueryWrapper.in(Product::getId, productIdSet);
        Map<Long, Product> productMap = productMapper.selectList(productQueryWrapper)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(User::getId, userIdSet);
        Map<Long, User> userMap = userMapper.selectList(userQueryWrapper)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return orderList.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(order.getId());
            orderVO.setOrderNo(order.getOrderNo());
            orderVO.setProductId(order.getProductId());
            orderVO.setBuyerId(order.getBuyerId());
            orderVO.setSellerId(order.getSellerId());
            orderVO.setPrice(order.getPrice());
            orderVO.setStatus(order.getStatus());
            orderVO.setCreateTime(order.getCreateTime());

            Product product = productMap.get(order.getProductId());
            if (product != null) {
                orderVO.setProductTitle(product.getTitle());
                orderVO.setProductImage(product.getImage());
            }

            User buyer = userMap.get(order.getBuyerId());
            if (buyer != null) {
                orderVO.setBuyerNickname(buyer.getNickname());
            }

            User seller = userMap.get(order.getSellerId());
            if (seller != null) {
                orderVO.setSellerNickname(seller.getNickname());
            }

            return orderVO;
        }).collect(Collectors.toList());
    }

    /**
     * 生成简单订单编号。
     *
     * 规则：当前时间戳 + 4位随机数。
     * 这种方式简单直观，适合教学项目。
     *
     * @return 订单编号
     */
    private String generateOrderNo() {
        int randomNumber = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ORD" + System.currentTimeMillis() + randomNumber;
    }
}
