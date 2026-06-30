package com.example.springskillaryback.common.util;

import com.example.springskillaryback.config.CookieConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieHandler {
    private final CookieConfig cookieConfig;

    public ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                             .httpOnly(cookieConfig.httpOnly())
                             .secure(cookieConfig.secure())
                             .sameSite(cookieConfig.sameSite())
                             .path(cookieConfig.path())
                             .maxAge(maxAge)
                             .build();
    }
}
