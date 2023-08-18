package com.usyd.capstone.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;

public class TokenUtils {

    private static final String SECRET_KEY = "your-secret-key";
    private static final long EXPIRATION_TIME = 86400000; // 有效期一天，单位：毫秒

    public static String generateToken(String userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return token;
    }

    public static boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // 检查过期时间
            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                return false; // Token 已过期
            }

            // 其他验证逻辑，根据实际需求添加

            return true; // Token 验证成功
        } catch (ExpiredJwtException ex) {
            return false; // Token 已过期
        } catch (Exception e) {
            return false; // Token 验证失败
        }
    }


    // get the userID from token
    public static String getUserIdFromToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // 获取 userId
            String userId = claims.getSubject();

            return userId;
        } catch (ExpiredJwtException ex) {
            return null; // Token 已过期
        } catch (Exception e) {
            return null; // Token 解析失败
        }
    }
}




