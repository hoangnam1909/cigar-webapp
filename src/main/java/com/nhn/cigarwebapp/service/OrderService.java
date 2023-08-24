package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.model.Order;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrderService {

    Page<OrderResponse> getOrders(Integer page, Integer size);

    OrderResponse getOrder(Long id);

    Order addOrder(OrderRequest request);

    void partialUpdateOrder(Long id, Map<String, Object> params);

}
