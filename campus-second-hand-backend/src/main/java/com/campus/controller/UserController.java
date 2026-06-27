package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.UserLoginDTO;
import com.campus.dto.UserRegisterDTO;
import com.campus.service.UserService;
import com.campus.vo.LoginVO;
import com.campus.vo.UserInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户控制层。
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 用户业务层。
     */
    @Resource
    private UserService userService;

    /**
     * 用户注册接口。
     *
     * @param registerDTO 注册参数
     * @return 统一返回结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功");
    }

    /**
     * 用户登录接口。
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody UserLoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 获取当前登录用户信息接口。
     *
     * @return 当前登录用户信息
     */
    @GetMapping("/current")
    public Result<UserInfoVO> currentUser() {
        UserInfoVO userInfoVO = userService.getCurrentUserInfo();
        return Result.success(userInfoVO);
    }
}
