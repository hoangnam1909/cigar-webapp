package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface OrderService {

    OrderResponse getOrder(@RequestParam Map<String, String> params);

    Page<OrderAdminResponse> getAdminOrders(@RequestParam Map<String, String> params);

    OrderAdminResponse getAdminOrder(Long id);

    Order addOrder(OrderRequest request);

    OrderAdminResponse partialUpdateOrder(Long id, Map<String, Object> params);

}
