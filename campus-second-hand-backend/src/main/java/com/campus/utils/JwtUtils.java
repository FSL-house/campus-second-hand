package com.campus.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类。
 *
 * 用于生成 token、解析 token、判断 token 是否有效。
 */
@Component
public class JwtUtils {

    /**
     * JWT 密钥。
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 过期时间，单位毫秒。
     */
    @Value("${jwt.expire}")
    private Long expire;

    /**
     * JWT 签名密钥对象。
     */
    private SecretKey secretKey;

    /**
     * 初始化签名密钥。
     *
     * 把配置文件中的字符串密钥转换成 JWT 可用的密钥对象。
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 token。
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @param role 用户角色
     * @return token 字符串
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析 token。
     *
     * @param token token 字符串
     * @return token 中的声明数据
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断 token 是否有效。
     *
     * @param token token 字符串
     * @return true 表示有效，false 表示无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
