package com.nhn.cigarwebapp.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCompanyAdminResponse implements Serializable {

    private Long id;
    private String name;

}
