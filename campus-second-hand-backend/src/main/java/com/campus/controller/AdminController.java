package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.AdminProductStatusDTO;
import com.campus.service.AdminService;
import com.campus.vo.AdminStatsVO;
import com.campus.vo.OrderVO;
import com.campus.vo.ProductVO;
import com.campus.vo.UserInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台管理控制层。
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * 后台管理业务层。
     */
    @Resource
    private AdminService adminService;

    /**
     * 查询所有用户接口。
     *
     * @return 用户列表
     */
    @GetMapping("/users")
    public Result<List<UserInfoVO>> users() {
        return Result.success(adminService.listAllUsers());
    }

    /**
     * 查询所有商品接口。
     *
     * @return 商品列表
     */
    @GetMapping("/products")
    public Result<List<ProductVO>> products() {
        return Result.success(adminService.listAllProducts());
    }

    /**
     * 管理员修改商品状态接口。
     *
     * 审核商品可传 ON_SALE，下架商品可传 OFF_SALE。
     *
     * @param statusDTO 商品状态参数
     * @return 统一返回结果
     */
    @PostMapping("/product/status")
    public Result<String> updateProductStatus(@RequestBody AdminProductStatusDTO statusDTO) {
        adminService.updateProductStatus(statusDTO);
        return Result.success("商品状态修改成功");
    }

    /**
     * 查询所有订单接口。
     *
     * @return 订单列表
     */
    @GetMapping("/orders")
    public Result<List<OrderVO>> orders() {
        return Result.success(adminService.listAllOrders());
    }

    /**
     * 查询后台统计数据接口。
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    public Result<AdminStatsVO> stats() {
        return Result.success(adminService.getStats());
    }
}
