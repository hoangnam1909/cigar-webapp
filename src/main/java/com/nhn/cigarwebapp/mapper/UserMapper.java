package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.UserInfoResponse;
import com.nhn.cigarwebapp.model.User;
import com.nhn.cigarwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
