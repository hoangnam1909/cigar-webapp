package com.nhn.cigarwebapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private List<OrderItemRequest> orderItems;

}
