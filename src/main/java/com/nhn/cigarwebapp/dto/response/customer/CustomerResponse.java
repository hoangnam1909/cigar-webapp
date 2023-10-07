package com.nhn.cigarwebapp.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse implements Serializable {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;

}
