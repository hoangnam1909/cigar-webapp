package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.OrderItem;
import com.nhn.cigarwebapp.model.OrderStatus;
import com.nhn.cigarwebapp.model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private CustomerResponse customer;
    private String note;
    private Date createdAt;
    private OrderStatus orderStatus;
    private Double totalPrice;
    private Shipment shipment;
    private Set<OrderItem> orderItems;

}
