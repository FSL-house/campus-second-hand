package com.campus.vo;

import lombok.Data;

/**
 * 后台统计数据返回对象。
 */
@Data
public class AdminStatsVO {

    /**
     * 用户总数。
     */
    private Long userCount;

    /**
     * 商品总数。
     */
    private Long productCount;

    /**
     * 待审核商品数。
     */
    private Long pendingProductCount;

    /**
     * 已上架商品数。
     */
    private Long onSaleProductCount;

    /**
     * 订单总数。
     */
    private Long orderCount;

    /**
     * 待交易订单数。
     */
    private Long pendingOrderCount;
}
