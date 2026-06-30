package com.example.springskillaryback.security.userpass;

import com.example.springskillaryback.domain.CustomUserDetails;
import com.example.springskillaryback.domain.Role;
import com.example.springskillaryback.domain.RoleEnum;
import com.example.springskillaryback.domain.User;
import com.example.springskillaryback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("The user could not be found or the user has no GrantedAuthority"));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                                                .map(Role::getRole)
                                                .map(RoleEnum::toString)
                                                .map(SimpleGrantedAuthority::new)        // String → GrantedAuthority
                                                .collect(Collectors.toSet());

        return CustomUserDetails.builder()
                                .username(user.getNickname())
                                .password(user.getPassword())
                                .authorities(authorities)
                                .accountNonExpired(true)
                                .accountNonLocked(true)
                                .credentialsNonExpired(true)
                                .enabled(true)
                                .build();
    }
}
