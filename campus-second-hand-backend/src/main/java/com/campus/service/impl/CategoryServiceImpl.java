package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.entity.Category;
import com.campus.mapper.CategoryMapper;
import com.campus.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类业务层实现类。
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * 分类 Mapper。
     */
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 查询所有分类。
     *
     * 按排序号升序排列，便于前端展示。
     *
     * @return 分类列表
     */
    @Override
    public List<Category> listAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSortNum).orderByAsc(Category::getId);
        return categoryMapper.selectList(queryWrapper);
    }
}
