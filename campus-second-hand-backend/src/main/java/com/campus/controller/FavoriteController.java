package com.campus.controller;

import com.campus.common.Result;
import com.campus.service.FavoriteService;
import com.campus.vo.ProductVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收藏控制层。
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    /**
     * 收藏业务层。
     */
    @Resource
    private FavoriteService favoriteService;

    /**
     * 收藏商品接口。
     *
     * @param productId 商品 ID
     * @return 统一返回结果
     */
    @PostMapping("/add/{productId}")
    public Result<String> add(@PathVariable Long productId) {
        favoriteService.addFavorite(productId);
        return Result.success("收藏成功");
    }

    /**
     * 取消收藏接口。
     *
     * @param productId 商品 ID
     * @return 统一返回结果
     */
    @DeleteMapping("/cancel/{productId}")
    public Result<String> cancel(@PathVariable Long productId) {
        favoriteService.cancelFavorite(productId);
        return Result.success("取消收藏成功");
    }

    /**
     * 查询我的收藏列表接口。
     *
     * @return 收藏商品列表
     */
    @GetMapping("/my")
    public Result<List<ProductVO>> myFavorites() {
        return Result.success(favoriteService.listMyFavorites());
    }
}
