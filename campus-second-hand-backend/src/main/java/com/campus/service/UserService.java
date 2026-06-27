package com.campus.service;

import com.campus.dto.UserLoginDTO;
import com.campus.dto.UserRegisterDTO;
import com.campus.vo.LoginVO;
import com.campus.vo.UserInfoVO;

/**
 * 用户业务层接口。
 */
public interface UserService {

    /**
     * 用户注册。
     *
     * @param registerDTO 注册参数
     */
    void register(UserRegisterDTO registerDTO);

    /**
     * 用户登录。
     *
     * @param loginDTO 登录参数
     * @return 登录返回结果
     */
    LoginVO login(UserLoginDTO loginDTO);

    /**
     * 获取当前登录用户信息。
     *
     * @return 当前用户信息
     */
    UserInfoVO getCurrentUserInfo();
}
