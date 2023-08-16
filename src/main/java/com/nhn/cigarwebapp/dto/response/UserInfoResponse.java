package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.Role;

public record UserInfoResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        Role role
) {
}
