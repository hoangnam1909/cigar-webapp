package com.nhn.cigarwebapp.dto.response.admin;

import com.nhn.cigarwebapp.dto.response.CustomerResponse;
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
public class OrderAdminResponse {

    private Long id;
    private CustomerResponse customer;
    private Date createdAt;
    private Double totalPrice;
    private String note;
    private Set<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private Shipment shipment;

}
