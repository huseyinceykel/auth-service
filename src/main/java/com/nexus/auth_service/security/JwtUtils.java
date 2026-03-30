package com.nexus.auth_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // application.properties içinden okunacak değerler
    @Value("${nexus.app.jwtSecret}")
    private String jwtSecret;

    @Value("${nexus.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Token Oluşturma (Generate)
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Token içinden Kullanıcı Adını Alma
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Token Doğrulama (Validate)
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Geçersiz JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token süresi dolmuş: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token desteklenmiyor: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string boş: {}", e.getMessage());
        }
        return false;
    }
}