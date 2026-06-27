package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.dto.ProductAddDTO;
import com.campus.entity.Category;
import com.campus.entity.Product;
import com.campus.entity.User;
import com.campus.mapper.CategoryMapper;
import com.campus.mapper.ProductMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.ProductService;
import com.campus.utils.LoginUser;
import com.campus.utils.UserContext;
import com.campus.vo.ProductVO;
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
 * 商品业务层实现类。
 */
@Service
public class ProductServiceImpl implements ProductService {

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
     * 发布商品。
     *
     * 主要逻辑：
     * 1. 校验商品参数
     * 2. 校验分类是否存在
     * 3. 获取当前登录用户
     * 4. 保存商品，默认状态为待审核
     *
     * @param productAddDTO 商品发布参数
     */
    @Override
    public void addProduct(ProductAddDTO productAddDTO) {
        if (productAddDTO == null) {
            throw new RuntimeException("商品参数不能为空");
        }
        if (!StringUtils.hasText(productAddDTO.getTitle())) {
            throw new RuntimeException("商品标题不能为空");
        }
        if (productAddDTO.getPrice() == null) {
            throw new RuntimeException("商品价格不能为空");
        }
        if (productAddDTO.getCategoryId() == null) {
            throw new RuntimeException("商品分类不能为空");
        }

        Category category = categoryMapper.selectById(productAddDTO.getCategoryId());
        if (category == null) {
            throw new RuntimeException("商品分类不存在");
        }

        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        Product product = new Product();
        product.setTitle(productAddDTO.getTitle());
        product.setDescription(productAddDTO.getDescription());
        product.setPrice(productAddDTO.getPrice());
        product.setImage(productAddDTO.getImage());
        product.setCategoryId(productAddDTO.getCategoryId());
        product.setUserId(userId);
        product.setStatus("PENDING");

        productMapper.insert(product);
    }

    /**
     * 查询公开商品列表。
     *
     * 主要逻辑：
     * 1. 只查询已上架商品
     * 2. 支持分类筛选
     * 3. 支持标题关键词搜索
     * 4. 把分类名和发布人昵称一起返回给前端
     *
     * @param categoryId 分类 ID，可为空
     * @param keyword 关键词，可为空
     * @return 商品列表
     */
    @Override
    public List<ProductVO> listProducts(Long categoryId, String keyword) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus, "ON_SALE");
        queryWrapper.eq(categoryId != null, Product::getCategoryId, categoryId);
        queryWrapper.like(StringUtils.hasText(keyword), Product::getTitle, keyword);
        queryWrapper.orderByDesc(Product::getCreateTime);

        List<Product> productList = productMapper.selectList(queryWrapper);
        return buildProductVOList(productList);
    }

    /**
     * 查询商品详情。
     *
     * 公开商品详情只允许查看已上架商品，
     * 避免未审核或已下架商品被直接访问。
     *
     * @param productId 商品 ID
     * @return 商品详情
     */
    @Override
    public ProductVO getProductDetail(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!"ON_SALE".equals(product.getStatus())) {
            throw new RuntimeException("该商品当前不可查看");
        }

        List<ProductVO> productVOList = buildProductVOList(Collections.singletonList(product));
        return productVOList.isEmpty() ? null : productVOList.get(0);
    }

    /**
     * 查询当前用户发布的商品。
     *
     * 这里不限制商品状态，因为用户需要看到自己发布的待审核、
     * 已上架、已下架商品。
     *
     * @return 商品列表
     */
    @Override
    public List<ProductVO> listMyProducts() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getUserId, userId);
        queryWrapper.orderByDesc(Product::getCreateTime);

        List<Product> productList = productMapper.selectList(queryWrapper);
        return buildProductVOList(productList);
    }

    /**
     * 把商品列表转换成前端使用的商品 VO 列表。
     *
     * 这里会批量查询分类和发布人，避免在循环里反复查数据库。
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
}
