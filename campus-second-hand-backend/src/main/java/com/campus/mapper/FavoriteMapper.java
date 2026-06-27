package com.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏数据访问层。
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
