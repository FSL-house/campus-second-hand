package com.campus.service;

import com.campus.vo.ProductVO;

import java.util.List;

/**
 * 收藏业务层接口。
 */
public interface FavoriteService {

    /**
     * 收藏商品。
     *
     * @param productId 商品 ID
     */
    void addFavorite(Long productId);

    /**
     * 取消收藏。
     *
     * @param productId 商品 ID
     */
    void cancelFavorite(Long productId);

    /**
     * 查询我的收藏列表。
     *
     * @return 收藏商品列表
     */
    List<ProductVO> listMyFavorites();
}
