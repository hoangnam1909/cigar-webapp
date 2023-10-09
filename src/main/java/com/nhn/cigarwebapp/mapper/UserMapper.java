package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.auth.UserInfoResponse;
import com.nhn.cigarwebapp.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserInfoResponse toResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

}
