package com.campus.service;

import com.campus.dto.ProductAddDTO;
import com.campus.vo.ProductVO;

import java.util.List;

/**
 * 商品业务层接口。
 */
public interface ProductService {

    /**
     * 发布商品。
     *
     * @param productAddDTO 商品发布参数
     */
    void addProduct(ProductAddDTO productAddDTO);

    /**
     * 查询商品列表。
     *
     * @param categoryId 分类 ID，可为空
     * @param keyword 关键词，可为空
     * @return 商品列表
     */
    List<ProductVO> listProducts(Long categoryId, String keyword);

    /**
     * 查询商品详情。
     *
     * @param productId 商品 ID
     * @return 商品详情
     */
    ProductVO getProductDetail(Long productId);

    /**
     * 查询当前用户发布的商品。
     *
     * @return 商品列表
     */
    List<ProductVO> listMyProducts();
}
