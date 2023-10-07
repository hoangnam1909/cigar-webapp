package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.auth.AuthenticationRequest;
import com.nhn.cigarwebapp.dto.request.auth.RefreshTokenRequest;
import com.nhn.cigarwebapp.dto.request.auth.RegisterRequest;
import com.nhn.cigarwebapp.dto.response.auth.AuthenticationResponse;


public interface AuthenticationService {

    public AuthenticationResponse refreshToken(RefreshTokenRequest request);

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

}
