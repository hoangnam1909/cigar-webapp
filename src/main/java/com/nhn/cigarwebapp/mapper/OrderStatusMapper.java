package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.order.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.order.OrderStatusResponse;
import com.nhn.cigarwebapp.entity.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusMapper {

    public OrderStatus toEntity(OrderStatusRequest request) {
        return OrderStatus.builder()
                .name(request.getName())
                .build();
    }

    public OrderStatusResponse toResponse(OrderStatus orderStatus) {
        return OrderStatusResponse.builder()
                .id(orderStatus.getId())
                .name(orderStatus.getName())
                .build();
    }

}
