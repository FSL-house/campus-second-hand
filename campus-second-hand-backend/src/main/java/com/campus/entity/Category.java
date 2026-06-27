package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体类。
 *
 * 对应数据库中的 category 表。
 */
@Data
@TableName("category")
public class Category {

    /**
     * 分类 ID。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称。
     */
    private String name;

    /**
     * 排序号。
     */
    @TableField("sort_num")
    private Integer sortNum;

    /**
     * 创建时间。
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
