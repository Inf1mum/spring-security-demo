package com.example.demospringsecurity.security;

import com.example.demospringsecurity.entity.User;
import com.example.demospringsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;

    public AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null) return null;

            return getAuthenticationToken(authHeader);
        } catch (Exception e) {
            log.error("Failed to get user profile. Error - {}", e.getMessage());
            return null;
        }
    }

    private AuthenticationToken getAuthenticationToken(String authHeader) {
        try {
            String[] authTokens = authHeader.split(" ");
            if (authTokens.length != 2) return null;

            if (authTokens[0].equalsIgnoreCase("Basic")) {
                return processBasicAuth(authTokens[1]);
            }

            return null;
        } catch (Exception e) {
            log.error("Can't parse authentication header {}. Error - {}", authHeader, e.getMessage());
            return null;
        }
    }

    private AuthenticationToken processBasicAuth(String authToken) {
        String[] userCredentials = new String(Base64.getDecoder().decode(authToken)).split(":");
        if (userCredentials.length != 2) return null;

        String email = userCredentials[0];
        String password = userCredentials[1];

        User user = userService.findByEmail(email);
        boolean isAuthenticated = user != null && Objects.equals(user.getPassword(), password);
        if (!isAuthenticated) return null;

        return AuthenticationToken.builder()
            .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
            .principal(user.getEmail())
            .authenticated(true)
            .authHeader("Basic %s".formatted(authToken))
            .user(user)
            .build();
    }

}
