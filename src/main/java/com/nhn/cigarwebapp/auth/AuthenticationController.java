package com.nhn.cigarwebapp.auth;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.config.JwtService;
import com.nhn.cigarwebapp.dto.request.RefreshTokenRequest;
import com.nhn.cigarwebapp.mapper.UserMapper;
import com.nhn.cigarwebapp.model.User;
import com.nhn.cigarwebapp.repository.UserRepository;
import com.nhn.cigarwebapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final HttpServletRequest servletRequest;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verify() {
        String accessToken = servletRequest.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(!jwtService.isTokenExpired(accessToken));
    }

    @GetMapping("/current-user/{token}")
    public ResponseEntity<ResponseObject> currentUser(@PathVariable String token) {
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (jwtService.isTokenValid(token, user.get()))
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Current user info")
                                .result(userMapper.toResponse(user.get()))
                                .build());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok().body(authenticationService.refreshToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
