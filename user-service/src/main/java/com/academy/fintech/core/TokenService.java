package com.academy.fintech.core;

import com.academy.fintech.core.db.refresh.token.RefreshToken;
import com.academy.fintech.core.db.refresh.token.RefreshTokenRepository;
import com.academy.fintech.core.db.user.User;
import com.academy.fintech.core.db.user.UserService;
import com.academy.fintech.core.exception.AuthorizationException;
import com.academy.fintech.core.util.JwtTokenUtil;
import com.academy.fintech.dto.TokenPair;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenPair generatePair(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new AuthorizationException("User not exists"));

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

        return new TokenPair(token, refreshToken);
    }

    public TokenPair refreshPair(String token) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(token);
        if (oldRefreshToken == null) {
            throw new AuthorizationException("Refresh token expired.");
        } else {
            refreshTokenRepository.delete(oldRefreshToken);
        }

        String username;
        try {
            username = jwtTokenUtil.getUsernameFromRefreshToken(token);
        } catch (ExpiredJwtException e) {
            throw new AuthorizationException("Refresh token expired.");
        }

        return generatePair(username);
    }
}
