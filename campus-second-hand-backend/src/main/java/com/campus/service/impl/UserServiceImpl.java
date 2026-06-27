package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.dto.UserLoginDTO;
import com.campus.dto.UserRegisterDTO;
import com.campus.entity.User;
import com.campus.mapper.UserMapper;
import com.campus.service.UserService;
import com.campus.utils.JwtUtils;
import com.campus.utils.UserContext;
import com.campus.vo.LoginVO;
import com.campus.vo.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 用户业务层实现类。
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用户 Mapper。
     */
    @Resource
    private UserMapper userMapper;

    /**
     * JWT 工具类。
     */
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 用户注册。
     *
     * 主要逻辑：
     * 1. 校验必填参数
     * 2. 判断用户名是否已存在
     * 3. 保存新用户
     *
     * @param registerDTO 注册参数
     */
    @Override
    public void register(UserRegisterDTO registerDTO) {
        if (registerDTO == null) {
            throw new RuntimeException("注册参数不能为空");
        }
        if (!StringUtils.hasText(registerDTO.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(registerDTO.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        if (!StringUtils.hasText(registerDTO.getNickname())) {
            throw new RuntimeException("昵称不能为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setNickname(registerDTO.getNickname());
        user.setPhone(registerDTO.getPhone());
        user.setRole("USER");

        userMapper.insert(user);
    }

    /**
     * 用户登录。
     *
     * 主要逻辑：
     * 1. 校验用户名和密码
     * 2. 查询数据库中的用户
     * 3. 生成 token
     * 4. 组装登录返回结果
     *
     * @param loginDTO 登录参数
     * @return 登录返回结果
     */
    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new RuntimeException("登录参数不能为空");
        }
        if (!StringUtils.hasText(loginDTO.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }

        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(convertToUserInfoVO(user));
        return loginVO;
    }

    /**
     * 获取当前登录用户信息。
     *
     * 主要逻辑：
     * 1. 从 UserContext 中拿到当前用户 ID
     * 2. 根据 ID 查询数据库
     * 3. 返回用户信息
     *
     * @return 当前用户信息
     */
    @Override
    public UserInfoVO getCurrentUserInfo() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return convertToUserInfoVO(user);
    }

    /**
     * 把用户实体类转换成前端需要的用户信息对象。
     *
     * 这里不返回密码，避免把敏感信息返回给前端。
     *
     * @param user 用户实体
     * @return 用户信息对象
     */
    private UserInfoVO convertToUserInfoVO(User user) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setRole(user.getRole());
        return userInfoVO;
    }
}
