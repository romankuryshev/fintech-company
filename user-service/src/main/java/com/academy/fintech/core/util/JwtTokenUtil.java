package com.academy.fintech.core.util;

import com.academy.fintech.dto.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.token.lifetime}")
    private Duration tokenLifeTime;

    @Value("${jwt.refresh.lifetime}")
    private Duration refreshLifeTime;

    private String generate(UserDetails userDetails, String secret, Duration lifeTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifeTime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSignIn(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generate(userDetails, tokenSecret, tokenLifeTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generate(userDetails, refreshSecret, refreshLifeTime);
    }

    public String getUsernameFromToken(String jwtToken) {
        return getAllClaimsFromToken(jwtToken, tokenSecret).getSubject();
    }

    public String getUsernameFromRefreshToken(String jwtToken) {
        return getAllClaimsFromToken(jwtToken, refreshSecret).getSubject();
    }

    public List<String> getAllRolesFromToken(String jwtToken) {
        return getAllClaimsFromToken(jwtToken, tokenSecret).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignIn(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignIn(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
