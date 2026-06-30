package com.example.springskillaryback.security;

import com.example.springskillaryback.security.userpass.UsernamePasswordAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class CustomUsernamePasswordFilter extends AbstractAuthenticationProcessingFilter {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private static final RequestMatcher DEFAULT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher
            .withDefaults().matcher(HttpMethod.POST, "/api/login");

    public CustomUsernamePasswordFilter() {
        super(DEFAULT_PATH_REQUEST_MATCHER);
    }

    public CustomUsernamePasswordFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        // Filtering
        if (!request.getMethod().equals("POST"))
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());

        // Parsing
        String username = request.getParameter(USERNAME_KEY);
        String password = request.getParameter(PASSWORD_KEY);
        if (username == null || password == null) username = password = "";

        // Auth - Manager 가 가공 할 만한 수준의 Authentication 으로 전환
        UsernamePasswordAuthentication authRequest
                = new UsernamePasswordAuthentication(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
