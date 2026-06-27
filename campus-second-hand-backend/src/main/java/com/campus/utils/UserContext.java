package com.campus.utils;

/**
 * 用户上下文工具类。
 *
 * 使用 ThreadLocal 保存当前请求的登录用户信息，
 * 这样在后面的业务代码里可以直接获取当前登录用户。
 */
public class UserContext {

    /**
     * 保存当前线程中的用户信息。
     */
    private static final ThreadLocal<LoginUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 保存当前登录用户。
     *
     * @param loginUser 当前登录用户
     */
    public static void setUser(LoginUser loginUser) {
        USER_THREAD_LOCAL.set(loginUser);
    }

    /**
     * 获取当前登录用户。
     *
     * @return 当前登录用户
     */
    public static LoginUser getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获取当前登录用户 ID。
     *
     * @return 用户 ID
     */
    public static Long getUserId() {
        LoginUser loginUser = USER_THREAD_LOCAL.get();
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * 清除当前线程中的用户信息，避免线程复用导致数据串线。
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
