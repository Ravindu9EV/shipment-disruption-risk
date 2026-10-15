package com.shipmentdisruptionrisk.sdrdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey signingKey;

    static JwtService forTesting(String secret, long expiration) {
        JwtService service = new JwtService();
        service.secret = secret;
        service.expiration = expiration;
        service.init();
        return service;
    }


    @PostConstruct
    void init(){

        this.signingKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username,String role){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return  Jwts.builder()
            .subject(username)
            .claim("role",role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(signingKey)
            .compact();
    }

    public String extractUsername(String token){
        return parseToken(token).getSubject();
    }

    public String extractRole(String token){
        return parseToken(token).get("role",String.class);
    }


    public boolean isTokenValid(String token){
        try{
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        }catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT:{}", e.getMessage());
            return false;
        }
    }

    private Claims parseToken(String token){
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
