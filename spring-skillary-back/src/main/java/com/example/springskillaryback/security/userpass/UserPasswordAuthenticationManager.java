package com.example.springskillaryback.security.userpass;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class UserPasswordAuthenticationManager implements AuthenticationManager {
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return daoAuthenticationProvider.authenticate(authentication);
    }
}
