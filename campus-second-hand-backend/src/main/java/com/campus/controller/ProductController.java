package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.ProductAddDTO;
import com.campus.service.ProductService;
import com.campus.vo.ProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品控制层。
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    /**
     * 商品业务层。
     */
    @Resource
    private ProductService productService;

    /**
     * 发布商品接口。
     *
     * @param productAddDTO 商品发布参数
     * @return 统一返回结果
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody ProductAddDTO productAddDTO) {
        productService.addProduct(productAddDTO);
        return Result.success("商品发布成功，等待管理员审核");
    }

    /**
     * 查询商品列表接口。
     *
     * 支持分类筛选和关键词搜索。
     *
     * @param categoryId 分类 ID，可为空
     * @param keyword 关键词，可为空
     * @return 商品列表
     */
    @GetMapping("/list")
    public Result<List<ProductVO>> list(@RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String keyword) {
        return Result.success(productService.listProducts(categoryId, keyword));
    }

    /**
     * 查询商品详情接口。
     *
     * @param id 商品 ID
     * @return 商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<ProductVO> detail(@PathVariable Long id) {
        return Result.success(productService.getProductDetail(id));
    }

    /**
     * 查询我发布的商品接口。
     *
     * @return 当前用户发布的商品列表
     */
    @GetMapping("/my")
    public Result<List<ProductVO>> myProducts() {
        return Result.success(productService.listMyProducts());
    }
}
