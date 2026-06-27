package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.dto.AdminProductStatusDTO;
import com.campus.entity.Category;
import com.campus.entity.Order;
import com.campus.entity.Product;
import com.campus.entity.User;
import com.campus.mapper.CategoryMapper;
import com.campus.mapper.OrderMapper;
import com.campus.mapper.ProductMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.AdminService;
import com.campus.utils.LoginUser;
import com.campus.utils.UserContext;
import com.campus.vo.AdminStatsVO;
import com.campus.vo.OrderVO;
import com.campus.vo.ProductVO;
import com.campus.vo.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 后台管理业务层实现类。
 */
@Service
public class AdminServiceImpl implements AdminService {

    /**
     * 用户 Mapper。
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 商品 Mapper。
     */
    @Resource
    private ProductMapper productMapper;

    /**
     * 分类 Mapper。
     */
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 订单 Mapper。
     */
    @Resource
    private OrderMapper orderMapper;

    /**
     * 查询所有用户。
     *
     * @return 用户列表
     */
    @Override
    public List<UserInfoVO> listAllUsers() {
        checkAdmin();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectList(queryWrapper)
                .stream()
                .map(this::convertToUserInfoVO)
                .collect(Collectors.toList());
    }

    /**
     * 查询所有商品。
     *
     * 管理员查看商品时不限制商品状态，
     * 这样可以看到待审核、已上架、已下架的全部商品。
     *
     * @return 商品列表
     */
    @Override
    public List<ProductVO> listAllProducts() {
        checkAdmin();
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Product::getCreateTime);
        List<Product> productList = productMapper.selectList(queryWrapper);
        return buildProductVOList(productList);
    }

    /**
     * 管理员修改商品状态。
     *
     * 这里支持两种核心操作：
     * 1. 审核商品：改成 ON_SALE
     * 2. 下架商品：改成 OFF_SALE
     *
     * @param statusDTO 状态参数
     */
    @Override
    public void updateProductStatus(AdminProductStatusDTO statusDTO) {
        checkAdmin();
        if (statusDTO == null) {
            throw new RuntimeException("商品参数不能为空");
        }
        if (statusDTO.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (!StringUtils.hasText(statusDTO.getStatus())) {
            throw new RuntimeException("商品状态不能为空");
        }

        String targetStatus = statusDTO.getStatus();
        if (!"ON_SALE".equals(targetStatus) && !"OFF_SALE".equals(targetStatus)) {
            throw new RuntimeException("商品状态只支持 ON_SALE 或 OFF_SALE");
        }

        Product product = productMapper.selectById(statusDTO.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        product.setStatus(targetStatus);
        productMapper.updateById(product);
    }

    /**
     * 查询所有订单。
     *
     * @return 订单列表
     */
    @Override
    public List<OrderVO> listAllOrders() {
        checkAdmin();
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Order::getCreateTime);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        return buildOrderVOList(orderList);
    }

    /**
     * 查询后台统计数据。
     *
     * @return 统计数据
     */
    @Override
    public AdminStatsVO getStats() {
        checkAdmin();

        AdminStatsVO adminStatsVO = new AdminStatsVO();
        adminStatsVO.setUserCount(userMapper.selectCount(null));
        adminStatsVO.setProductCount(productMapper.selectCount(null));
        adminStatsVO.setPendingProductCount(countProductsByStatus("PENDING"));
        adminStatsVO.setOnSaleProductCount(countProductsByStatus("ON_SALE"));
        adminStatsVO.setOrderCount(orderMapper.selectCount(null));
        adminStatsVO.setPendingOrderCount(countOrdersByStatus("PENDING_DEAL"));
        return adminStatsVO;
    }

    /**
     * 校验当前登录用户是否为管理员。
     */
    private void checkAdmin() {
        LoginUser loginUser = UserContext.getUser();
        if (loginUser == null) {
            throw new RuntimeException("请先登录");
        }
        if (!"ADMIN".equals(loginUser.getRole())) {
            throw new RuntimeException("只有管理员可以操作");
        }
    }

    /**
     * 按商品状态统计数量。
     *
     * @param status 商品状态
     * @return 数量
     */
    private Long countProductsByStatus(String status) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus, status);
        return productMapper.selectCount(queryWrapper);
    }

    /**
     * 按订单状态统计数量。
     *
     * @param status 订单状态
     * @return 数量
     */
    private Long countOrdersByStatus(String status) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStatus, status);
        return orderMapper.selectCount(queryWrapper);
    }

    /**
     * 把用户实体转换成前端需要的用户信息对象。
     *
     * @param user 用户实体
     * @return 用户信息对象
     */
    private UserInfoVO convertToUserInfoVO(User user) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setRole(user.getRole());
        return userInfoVO;
    }

    /**
     * 把商品实体列表转换成商品 VO 列表。
     *
     * @param productList 商品实体列表
     * @return 商品 VO 列表
     */
    private List<ProductVO> buildProductVOList(List<Product> productList) {
        if (productList == null || productList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> categoryIdSet = productList.stream()
                .map(Product::getCategoryId)
                .collect(Collectors.toSet());
        Set<Long> userIdSet = productList.stream()
                .map(Product::getUserId)
                .collect(Collectors.toSet());

        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper.in(Category::getId, categoryIdSet);
        Map<Long, Category> categoryMap = categoryMapper.selectList(categoryQueryWrapper)
                .stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(User::getId, userIdSet);
        Map<Long, User> userMap = userMapper.selectList(userQueryWrapper)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return productList.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            productVO.setId(product.getId());
            productVO.setTitle(product.getTitle());
            productVO.setDescription(product.getDescription());
            productVO.setPrice(product.getPrice());
            productVO.setImage(product.getImage());
            productVO.setCategoryId(product.getCategoryId());
            productVO.setUserId(product.getUserId());
            productVO.setStatus(product.getStatus());
            productVO.setCreateTime(product.getCreateTime());

            Category category = categoryMap.get(product.getCategoryId());
            if (category != null) {
                productVO.setCategoryName(category.getName());
            }

            User user = userMap.get(product.getUserId());
            if (user != null) {
                productVO.setSellerNickname(user.getNickname());
            }

            return productVO;
        }).collect(Collectors.toList());
    }

    /**
     * 把订单实体列表转换成订单 VO 列表。
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
}
