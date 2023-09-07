package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.auth.AuthenticationRequest;
import com.nhn.cigarwebapp.auth.AuthenticationResponse;
import com.nhn.cigarwebapp.auth.RegisterRequest;
import com.nhn.cigarwebapp.config.JwtRefreshTokenService;
import com.nhn.cigarwebapp.config.JwtService;
import com.nhn.cigarwebapp.dto.request.RefreshTokenRequest;
import com.nhn.cigarwebapp.model.Role;
import com.nhn.cigarwebapp.model.User;
import com.nhn.cigarwebapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtRefreshTokenService.extractUsername(request.getRefreshToken());
        var user = userRepository.findByUsername(username)
                .orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().toString());
        var jwtToken = jwtService.generateToken(claims, user);
        var refreshToken = jwtRefreshTokenService.generateRefreshToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().toString());
        var jwtToken = jwtService.generateToken(claims, user);
        var refreshToken = jwtRefreshTokenService.generateRefreshToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

}
