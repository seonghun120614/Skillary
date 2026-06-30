package com.example.springskillaryback.security.userpass;

import com.example.springskillaryback.common.util.CookieHandler;
import com.example.springskillaryback.common.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final CookieHandler cookieHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) {
        String name = (String) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String accessToken = jwtProvider.createAccessToken(name, authorities);
        String refreshToken = jwtProvider.createRefreshToken(name, authorities);

        var accessCookie = cookieHandler.createCookie("access_token",
                                                                 accessToken,
                                                                 jwtProvider.getAccessExpirySeconds());
        var refreshCookie = cookieHandler.createCookie("refresh_token",
                                                       refreshToken,
                                                       jwtProvider.getRefreshExpirySeconds());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
