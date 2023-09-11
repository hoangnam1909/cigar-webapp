package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;
    private final OrderItemMapper orderItemMapper;

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
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .note(order.getNote())
                .orderItems(orderItemMapper.toResponses(order.getOrderItems()))
                .shipment(order.getShipment())
                .build();
    }

}
