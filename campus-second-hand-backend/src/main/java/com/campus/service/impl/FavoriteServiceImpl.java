package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.entity.Category;
import com.campus.entity.Favorite;
import com.campus.entity.Product;
import com.campus.entity.User;
import com.campus.mapper.CategoryMapper;
import com.campus.mapper.FavoriteMapper;
import com.campus.mapper.ProductMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.FavoriteService;
import com.campus.utils.UserContext;
import com.campus.vo.ProductVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收藏业务层实现类。
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    /**
     * 收藏 Mapper。
     */
    @Resource
    private FavoriteMapper favoriteMapper;

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
     * 用户 Mapper。
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 收藏商品。
     *
     * 主要逻辑：
     * 1. 校验商品是否存在
     * 2. 校验商品是否可收藏
     * 3. 校验是否已经收藏过
     * 4. 保存收藏记录
     *
     * @param productId 商品 ID
     */
    @Override
    public void addFavorite(Long productId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!"ON_SALE".equals(product.getStatus())) {
            throw new RuntimeException("当前商品不能收藏");
        }

        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        if (favorite != null) {
            throw new RuntimeException("你已经收藏过该商品");
        }

        Favorite newFavorite = new Favorite();
        newFavorite.setUserId(userId);
        newFavorite.setProductId(productId);
        favoriteMapper.insert(newFavorite);
    }

    /**
     * 取消收藏。
     *
     * 只有收藏记录存在时才允许取消。
     *
     * @param productId 商品 ID
     */
    @Override
    public void cancelFavorite(Long productId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        if (favorite == null) {
            throw new RuntimeException("你还没有收藏该商品");
        }

        favoriteMapper.deleteById(favorite.getId());
    }

    /**
     * 查询我的收藏列表。
     *
     * 这里返回商品信息列表，便于前端直接展示收藏商品卡片。
     *
     * @return 收藏商品列表
     */
    @Override
    public List<ProductVO> listMyFavorites() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        LambdaQueryWrapper<Favorite> favoriteQueryWrapper = new LambdaQueryWrapper<>();
        favoriteQueryWrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        List<Favorite> favoriteList = favoriteMapper.selectList(favoriteQueryWrapper);
        if (favoriteList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = favoriteList.stream()
                .map(Favorite::getProductId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<Product> productQueryWrapper = new LambdaQueryWrapper<>();
        productQueryWrapper.in(Product::getId, productIds);
        List<Product> productList = productMapper.selectList(productQueryWrapper);
        if (productList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Integer> favoriteOrderMap = buildFavoriteOrderMap(productIds);
        List<ProductVO> productVOList = buildProductVOList(productList);
        productVOList.sort((a, b) -> favoriteOrderMap.getOrDefault(a.getId(), Integer.MAX_VALUE)
                - favoriteOrderMap.getOrDefault(b.getId(), Integer.MAX_VALUE));
        return productVOList;
    }

    /**
     * 构建收藏顺序映射。
     *
     * 因为批量查询商品后顺序可能变化，
     * 所以这里按收藏时间对应的商品顺序重新排序。
     *
     * @param productIds 收藏商品 ID 列表
     * @return 商品顺序映射
     */
    private Map<Long, Integer> buildFavoriteOrderMap(List<Long> productIds) {
        return java.util.stream.IntStream.range(0, productIds.size())
                .boxed()
                .collect(Collectors.toMap(productIds::get, Function.identity(), (a, b) -> a));
    }

    /**
     * 把商品实体列表转换成前端需要的商品信息列表。
     *
     * @param productList 商品实体列表
     * @return 商品 VO 列表
     */
    private List<ProductVO> buildProductVOList(List<Product> productList) {
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
}
