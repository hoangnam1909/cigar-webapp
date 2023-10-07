package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.auth.UserInfoResponse;
import com.nhn.cigarwebapp.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserInfoResponse toResponse(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }

}
