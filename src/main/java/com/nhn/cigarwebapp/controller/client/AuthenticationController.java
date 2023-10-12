package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.config.JwtService;
import com.nhn.cigarwebapp.dto.request.auth.AuthenticationRequest;
import com.nhn.cigarwebapp.dto.request.auth.RefreshTokenRequest;
import com.nhn.cigarwebapp.dto.request.auth.RegisterRequest;
import com.nhn.cigarwebapp.dto.response.auth.AuthenticationResponse;
import com.nhn.cigarwebapp.dto.response.auth.UserInfoResponse;
import com.nhn.cigarwebapp.mapper.UserMapper;
import com.nhn.cigarwebapp.repository.UserRepository;
import com.nhn.cigarwebapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserInfoResponse userInfoResponse = authenticationService.currentUser(token);
        if (userInfoResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Current user info")
                            .result(userInfoResponse)
                            .build());

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
