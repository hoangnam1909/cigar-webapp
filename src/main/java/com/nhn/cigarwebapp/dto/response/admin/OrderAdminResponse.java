package com.nhn.cigarwebapp.dto.response.admin;

import com.nhn.cigarwebapp.dto.response.customer.CustomerResponse;
import com.nhn.cigarwebapp.dto.response.order.OrderItemResponse;
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
public class OrderAdminResponse implements Serializable {

    private Long id;
    private CustomerResponse customer;
    private Date createdAt;
    private Double totalPrice;
    private String note;
    private List<OrderItemResponse> orderItems;
    private OrderStatus orderStatus;
    private Shipment shipment;
    private PaymentResponse payment;


}
