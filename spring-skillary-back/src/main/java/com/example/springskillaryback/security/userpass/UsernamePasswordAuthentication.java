package com.example.springskillaryback.security.userpass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {
    Collection<? extends GrantedAuthority> authorities;
    Object credentials; // passwd
    Object principal;
    @Setter(AccessLevel.PRIVATE)
    boolean authenticated;

    // Options
    Object details;
    String name;

    public UsernamePasswordAuthentication(@Nullable Object principal,
                                          @Nullable Object credentials) {
        super(principal, credentials);
    }

    public UsernamePasswordAuthentication(Object principal,
                                          @Nullable Object credentials,
                                          Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
