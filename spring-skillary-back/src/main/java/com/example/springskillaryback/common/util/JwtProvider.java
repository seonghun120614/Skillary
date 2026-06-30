package com.example.springskillaryback.common.util;

import com.example.springskillaryback.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private SecretKey key;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(
                jwtConfig.secret().getBytes()
        );
    }

    public String createAccessToken(String username,
                                    Collection<? extends GrantedAuthority> authorities) {
        return createToken(username, authorities, jwtConfig.accessExpireMilliSeconds());
    }

    public String createRefreshToken(String username,
                                     Collection<? extends GrantedAuthority> authorities) {
        return createToken(username, authorities, jwtConfig.refreshExpireMilliSeconds());
    }

    private String createToken(String username,
                              Collection<? extends GrantedAuthority> authorities,
                              long expireMilliSeconds) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMilliSeconds);

        String roles = authorities.stream()
                                  .map(GrantedAuthority::getAuthority)
                                  .collect(Collectors.joining(","));

        return Jwts.builder()
                   .subject(username)
                   .claim("roles", roles)
                   .issuedAt(now)
                   .expiration(expiry)
                   .signWith(key)
                   .compact();
    }

    public long getAccessExpirySeconds() {
        return jwtConfig.accessExpireMilliSeconds();
    }

    public long getRefreshExpirySeconds() {
        return jwtConfig.refreshExpireMilliSeconds();
    }
}
