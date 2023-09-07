package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrderService {

    Page<OrderResponse> getOrders(Integer page, Integer size);

    Page<OrderAdminResponse> getAdminOrders(OrderSpecification specification, Integer page, Integer size, String sort);

    OrderResponse getOrder(Long id);

    Order addOrder(OrderRequest request);

    void partialUpdateOrder(Long id, Map<String, Object> params);

}
