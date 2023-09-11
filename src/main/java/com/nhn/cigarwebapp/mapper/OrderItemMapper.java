package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.OrderItemResponse;
import com.nhn.cigarwebapp.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ProductMapper productMapper;

    public OrderItemResponse toResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .product(productMapper.toResponse(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .createdAt(orderItem.getCreatedAt())
                .build();
    }

    public List<OrderItemResponse> toResponses(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(this::toResponse)
                .toList();
    }

}
