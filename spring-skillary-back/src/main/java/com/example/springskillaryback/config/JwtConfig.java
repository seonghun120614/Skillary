package com.example.springskillaryback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfig(
        String secret,
        long accessExpireMilliSeconds,
        long refreshExpireMilliSeconds
) {}