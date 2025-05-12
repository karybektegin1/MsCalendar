package com.appointmentService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351"; // 256-bit key

    private static JwtParser getParser() {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public static Claims extractClaims(String token) {
        return getParser()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public static Long getUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    public static String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public static boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }
}
