package com.nhn.cigarwebapp.dto.response.auth;

import com.nhn.cigarwebapp.entity.Role;

public record UserInfoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        Role role
) {
}
