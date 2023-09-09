package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.OrderStatusResponse;
import com.nhn.cigarwebapp.mapper.OrderStatusMapper;
import com.nhn.cigarwebapp.model.OrderStatus;
import com.nhn.cigarwebapp.repository.OrderStatusRepository;
import com.nhn.cigarwebapp.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Cacheable("orderStatuses")
    public List<OrderStatusResponse> getOrderStatuses() {
        return orderStatusRepository.findAll()
                .stream()
                .map(orderStatusMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "orderStatuses", allEntries = true)
    public OrderStatusResponse add(OrderStatusRequest request) {
        try {
            OrderStatus orderStatus = orderStatusMapper.toEntity(request);
            return orderStatusMapper.toResponse(orderStatusRepository.save(orderStatus));
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            return null;
        }
    }

//    @Override
//    @Transactional
//    public void add(List<OrderStatusRequest> requestList) {
//        try {
//            requestList
//                    .forEach(request ->
//                            orderStatusRepository.saveAndFlush(orderStatusMapper.toEntity(request)));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

}
