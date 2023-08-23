package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.Address;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private List<Address> addresses;

}
