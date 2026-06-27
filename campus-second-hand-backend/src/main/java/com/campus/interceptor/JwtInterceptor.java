package com.campus.interceptor;

import com.campus.common.Result;
import com.campus.utils.JwtUtils;
import com.campus.utils.LoginUser;
import com.campus.utils.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT 登录拦截器。
 *
 * 作用：
 * 1. 判断请求头中是否携带 token
 * 2. 校验 token 是否有效
 * 3. 把当前登录用户保存到 UserContext 中
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * JWT 工具类。
     */
    @Resource
    private JwtUtils jwtUtils;

    /**
     * JSON 转换工具。
     */
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 在进入 Controller 之前执行。
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器
     * @return true 表示放行，false 表示拦截
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            writeErrorResponse(response, "请先登录");
            return false;
        }

        if (!jwtUtils.validateToken(token)) {
            writeErrorResponse(response, "登录已过期，请重新登录");
            return false;
        }

        Claims claims = jwtUtils.parseToken(token);
        Long userId = ((Number) claims.get("userId")).longValue();
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");

        LoginUser loginUser = new LoginUser(userId, username, role);
        UserContext.setUser(loginUser);
        return true;
    }

    /**
     * 请求完成后执行，用于清理线程中的用户信息。
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器
     * @param ex 异常对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

    /**
     * 向前端返回统一的未登录错误信息。
     *
     * @param response 响应对象
     * @param message 提示消息
     * @throws IOException IO 异常
     */
    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(401, message)));
    }
}
