package com.nhn.cigarwebapp.dto.response.order;

import com.nhn.cigarwebapp.dto.response.customer.CustomerResponse;
import com.nhn.cigarwebapp.dto.response.payment.PaymentResponse;
import com.nhn.cigarwebapp.entity.OrderStatus;
import com.nhn.cigarwebapp.entity.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse implements Serializable {

    private Long id;
    private CustomerResponse customer;
    private String note;
    private Date createdAt;
    private OrderStatus orderStatus;
    private Double totalPrice;
    private Shipment shipment;
    private List<OrderItemResponse> orderItems;
    private PaymentResponse payment;

}
