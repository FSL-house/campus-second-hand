package com.campus.controller;

import com.campus.common.Result;
import com.campus.entity.Category;
import com.campus.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类控制层。
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    /**
     * 分类业务层。
     */
    @Resource
    private CategoryService categoryService;

    /**
     * 查询分类列表接口。
     *
     * @return 分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.listAll());
    }
}
