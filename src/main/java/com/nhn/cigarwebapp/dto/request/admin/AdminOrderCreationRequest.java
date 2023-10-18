package com.nhn.cigarwebapp.dto.request.admin;

import com.nhn.cigarwebapp.dto.request.order.OrderItemRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderCreationRequest {

    private String fullName;
    private String phone;
    private String email;
    private String deliveryAddress;
    private String note;
    private List<OrderItemRequest> orderItems;
    private String paymentDestinationId;
    private Long orderStatusId;
    private Long deliveryCompanyId;

}
