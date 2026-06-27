package com.campus.config;

import com.campus.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Spring MVC 配置类。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * JWT 拦截器。
     */
    @Resource
    private JwtInterceptor jwtInterceptor;

    /**
     * 注册拦截器。
     *
     * 这里先放行登录、注册和商品公开查询接口，
     * 后面新增接口时再继续补充放行路径。
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/register",
                        "/user/login",
                        "/product/list",
                        "/product/detail/**",
                        "/category/list"
                );
    }

    /**
     * 配置跨域。
     *
     * 前后端分离开发时，前端和后端端口不同，
     * 所以需要开启跨域访问。
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
