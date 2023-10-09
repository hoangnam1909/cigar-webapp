package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.auth.AuthenticationRequest;
import com.nhn.cigarwebapp.dto.request.auth.RefreshTokenRequest;
import com.nhn.cigarwebapp.dto.request.auth.RegisterRequest;
import com.nhn.cigarwebapp.dto.response.auth.AuthenticationResponse;
import com.nhn.cigarwebapp.dto.response.auth.UserInfoResponse;


public interface AuthenticationService {

     AuthenticationResponse refreshToken(RefreshTokenRequest request);

     AuthenticationResponse register(RegisterRequest request);

     AuthenticationResponse authenticate(AuthenticationRequest request);

    UserInfoResponse currentUser(String token);

}
