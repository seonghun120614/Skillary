package com.example.springskillaryback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookie")
public record CookieConfig(
        boolean httpOnly,
        boolean secure,     // Local, Prod: false, https 발급시 true
        String sameSite,    // Local, Prod: Lax, https 발급시 None
        String path
) {}