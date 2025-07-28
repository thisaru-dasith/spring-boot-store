package com.codewithmosh.store.service;

import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private String secret = "jNI+VDuaGZtKnJj2sEuJ5ucTNci5RYjmcw16c83Et3yr/9vMsguT3HuHZ2u5bZMF";

    public String generateJwtToken(User user){
        //1 day
        System.out.println(secret);
        final long expirationDate = 60 * 60 * 24;
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * expirationDate))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }

    public boolean validateJwtToken(String token) {

        try{
            var claims = getClaimsFromJwtToken(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }



    }

    public Long getIdFromJwtToken(String token){
        return Long.valueOf(getClaimsFromJwtToken(token).getSubject());
    }

    public Claims getClaimsFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
