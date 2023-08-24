package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.OrderStatusResponse;

import java.util.List;

public interface OrderStatusService {

    List<OrderStatusResponse> getOrderStatuses();

    void addOrderStatuses(List<OrderStatusRequest> requestList);

}
