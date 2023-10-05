package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.AuthenticationRequest;
import com.nhn.cigarwebapp.dto.response.AuthenticationResponse;
import com.nhn.cigarwebapp.dto.request.RegisterRequest;
import com.nhn.cigarwebapp.dto.request.RefreshTokenRequest;


public interface AuthenticationService {

    public AuthenticationResponse refreshToken(RefreshTokenRequest request);

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

}
