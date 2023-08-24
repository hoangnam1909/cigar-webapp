package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.OrderStatusResponse;
import com.nhn.cigarwebapp.mapper.OrderStatusMapper;
import com.nhn.cigarwebapp.repository.OrderStatusRepository;
import com.nhn.cigarwebapp.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderStatusResponse> getOrderStatuses() {
        return orderStatusRepository.findAll()
                .stream()
                .map(orderStatus -> orderStatusMapper.toResponse(orderStatus))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addOrderStatuses(List<OrderStatusRequest> requestList) {
        try {
            requestList
                    .forEach(request ->
                            orderStatusRepository.saveAndFlush(orderStatusMapper.toEntity(request)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
