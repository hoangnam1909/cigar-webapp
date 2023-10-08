package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.order.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.order.OrderStatusResponse;
import com.nhn.cigarwebapp.entity.OrderStatus;
import com.nhn.cigarwebapp.mapper.OrderStatusMapper;
import com.nhn.cigarwebapp.repository.OrderStatusRepository;
import com.nhn.cigarwebapp.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderStatusMapper orderStatusMapper;

    @Override
    @Cacheable("List<OrderStatusResponse>")
    public List<OrderStatusResponse> getOrderStatuses() {
        return orderStatusRepository.findAll()
                .stream()
                .map(orderStatusMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "List<OrderStatusResponse>", allEntries = true)
    public OrderStatusResponse add(OrderStatusRequest request) {
        try {
            OrderStatus orderStatus = orderStatusMapper.toEntity(request);
            return orderStatusMapper.toResponse(orderStatusRepository.save(orderStatus));
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            return null;
        }
    }

}
