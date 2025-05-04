package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 令牌生成的秘钥
    public static final String SECRET_KEY = "21505wang";
    // 令牌有效时间1小时
    public static final long EXPIRATION_TIME = 60 * 60 * 1000;

    // 静态方法,生成一个全新的令牌并返回
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    // 静态方法,解析令牌
    public static Claims parseToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
