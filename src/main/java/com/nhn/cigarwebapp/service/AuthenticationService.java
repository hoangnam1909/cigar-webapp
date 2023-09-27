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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public interface AuthenticationService {

    public AuthenticationResponse refreshToken(RefreshTokenRequest request);

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

}
