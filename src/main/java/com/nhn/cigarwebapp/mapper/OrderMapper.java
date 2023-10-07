package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.dto.response.order.OrderResponse;
import com.nhn.cigarwebapp.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customer(customerMapper.toResponse(order.getCustomer()))
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .note(order.getNote())
                .orderItems(orderItemMapper.toResponses(order.getOrderItems()))
                .shipment(order.getShipment())
                .build();
    }

    public OrderAdminResponse toAdminResponse(Order order) {
        return OrderAdminResponse.builder()
                .id(order.getId())
                .customer(customerMapper.toResponse(order.getCustomer()))
                .createdAt(order.getCreatedAt())
                .totalPrice(order.getTotalPrice())
                .note(order.getNote())
                .orderItems(orderItemMapper.toResponses(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .shipment(order.getShipment())
                .payment(paymentMapper.toResponse(order.getPayment()))
                .build();
    }

}
