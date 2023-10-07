package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.order.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.order.OrderStatusResponse;

import java.util.List;

public interface OrderStatusService {

    List<OrderStatusResponse> getOrderStatuses();

    OrderStatusResponse add(OrderStatusRequest request);

//    void add(List<OrderStatusRequest> requestList);

}
