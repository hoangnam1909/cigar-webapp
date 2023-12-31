package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.admin.AdminOrderCreationRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderItemRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderWithPaymentRequest;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.dto.response.order.OrderResponse;
import com.nhn.cigarwebapp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderResponse getOrder(@RequestParam Map<String, String> params);

    Page<OrderAdminResponse> getAdminOrders(@RequestParam Map<String, String> params);

    OrderAdminResponse getAdminOrder(Long id);

    boolean checkProductsIsValidForOrder(List<OrderItemRequest> orderItems);

    Order addOrder(OrderWithPaymentRequest request);

    Order adminAddOrder(AdminOrderCreationRequest request);

    Order partialUpdateOrder(Long id, Map<String, Object> params);

    void deleteOrder(Long id);

}
