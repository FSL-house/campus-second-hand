package com.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品数据访问层。
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
