package com.nhn.cigarwebapp.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithPaymentRequest {

    private String fullName;
    private String phone;
    private String email;
    private String deliveryAddress;
    private String note;
    private List<OrderItemRequest> orderItems;
    private String paymentDestinationId;

}
