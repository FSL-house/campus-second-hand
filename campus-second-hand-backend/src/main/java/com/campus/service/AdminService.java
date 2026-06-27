package com.campus.service;

import com.campus.dto.AdminProductStatusDTO;
import com.campus.vo.AdminStatsVO;
import com.campus.vo.OrderVO;
import com.campus.vo.ProductVO;
import com.campus.vo.UserInfoVO;

import java.util.List;

/**
 * 后台管理业务层接口。
 */
public interface AdminService {

    /**
     * 查询所有用户。
     *
     * @return 用户列表
     */
    List<UserInfoVO> listAllUsers();

    /**
     * 查询所有商品。
     *
     * @return 商品列表
     */
    List<ProductVO> listAllProducts();

    /**
     * 管理员修改商品状态。
     *
     * @param statusDTO 状态参数
     */
    void updateProductStatus(AdminProductStatusDTO statusDTO);

    /**
     * 查询所有订单。
     *
     * @return 订单列表
     */
    List<OrderVO> listAllOrders();

    /**
     * 查询后台统计数据。
     *
     * @return 统计数据
     */
    AdminStatsVO getStats();
}
