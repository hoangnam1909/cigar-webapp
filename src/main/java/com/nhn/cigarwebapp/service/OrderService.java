package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrders();

    Order addOrder(OrderRequest request);

}
