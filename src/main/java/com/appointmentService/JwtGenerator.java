package com.appointmentService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;

public class JwtGenerator {

    public static void main(String[] args) {
        String secret = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351"; // минимум 32 символа (256 бит)
        String email = "karybekov03@mail.ru";

        String token = Jwts.builder()
                .setSubject(String.valueOf(email)) // идентификатор врача
                .claim("role", "DOCTOR")
                .claim("userId", 1L)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(3600*24))) // 1 час
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated JWT token:\n" + token);
    }
}