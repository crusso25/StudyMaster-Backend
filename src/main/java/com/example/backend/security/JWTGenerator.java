package com.example.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.example.backend.model.UserEntity;
import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Use a strong key
    private final long ACCESS_TOKEN_EXPIRATION = 3600000; // 1 hour
    private final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days

    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        return generateToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateAccessToken(UserEntity user) {
        String username = user.getUsername();
        return generateToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(UserEntity user) {
        String username = user.getUsername();
        return generateToken(username, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String username, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
