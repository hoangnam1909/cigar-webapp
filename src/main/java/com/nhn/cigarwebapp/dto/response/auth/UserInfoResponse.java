package com.nhn.cigarwebapp.dto.response.auth;

import com.nhn.cigarwebapp.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserInfoResponse implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Role role;

}
