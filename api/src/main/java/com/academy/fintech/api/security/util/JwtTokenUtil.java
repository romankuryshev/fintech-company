package com.academy.fintech.api.security.util;

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

    @Value("${jwt.token-secret}")
    private String tokenSecret;

    public String getUsernameFromToken(String jwtToken) {
        return getAllClaimsFromToken(jwtToken).getSubject();
    }

    public List<String> getAllRolesFromToken(String jwtToken) {
        return getAllClaimsFromToken(jwtToken).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignIn())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignIn() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes());
    }
}
