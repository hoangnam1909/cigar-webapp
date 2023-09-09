package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.mapper.CustomerMapper;
import com.nhn.cigarwebapp.mapper.OrderMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.model.*;
import com.nhn.cigarwebapp.repository.*;
import com.nhn.cigarwebapp.service.OrderService;
import com.nhn.cigarwebapp.service.ShipmentService;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final SortMapper sortMapper;
    private final ShipmentService shipmentService;
    private final ShipmentRepository shipmentRepository;
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    @Override
    public Page<OrderResponse> getOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        return orderRepository.findAll(pageable)
                .map(order -> orderMapper.toResponse(order));
    }

    @Override
    public Page<OrderAdminResponse> getAdminOrders(OrderSpecification specification, Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getOrderSort(sort));
        return orderRepository.findAll(specification, pageable)
                .map(order -> orderMapper.toAdminResponse(order));
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> orderMapper.toResponse(value)).orElse(null);
    }

    @Override
    @Transactional
    public Order addOrder(OrderRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findByPhone(request.getPhone());
        Customer customer;

        if (optionalCustomer.isPresent()) {
            customer = optionalCustomer.get();
        } else {
            customer = customerMapper.toEntity(request);
            customerRepository.saveAndFlush(customer);
        }

        AtomicReference<Double> total = new AtomicReference<>(0.0);
        request.getOrderItems()
                .forEach(orderItemRequest -> {
                    Optional<Product> product = productRepository.findById(orderItemRequest.getProductId());
                    product.ifPresent(value -> total.updateAndGet(v -> v + value.getSalePrice() * orderItemRequest.getQuantity()));
                });

        Order order = Order.builder()
                .customer(customerRepository.getReferenceById(customer.getId()))
                .orderStatus(orderStatusRepository.getReferenceById(1L))
                .shipment(shipmentService.add(request.getAddress()))
                .totalPrice(total.get())
                .note(request.getNote())
                .build();
        orderRepository.saveAndFlush(order);

        request.getOrderItems()
                .forEach(orderItemRequest -> {
                    orderItemRepository
                            .saveAndFlush(OrderItem.builder()
                                    .order(order)
                                    .product(productRepository.getReferenceById(orderItemRequest.getProductId()))
                                    .quantity(orderItemRequest.getQuantity())
                                    .build());
                });

        return order;
    }

    @Override
    @Transactional
    public void partialUpdateOrder(Long id, Map<String, Object> params) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (params.containsKey("orderStatusId")) {
                order.setOrderStatus(
                        orderStatusRepository
                                .getReferenceById(Long.valueOf((String) params.get("orderStatusId"))));
            }

            if (params.containsKey("trackingNumber")) {
                String trackingNumber = (String) params.get("trackingNumber");
                Shipment shipment = order.getShipment();
                shipment.setTrackingNumber(trackingNumber);
                shipmentRepository.save(shipment);
            }

            if (params.containsKey("deliveryCompanyId")) {
                String deliveryCompanyIdStr = (String) params.get("deliveryCompanyId");
                if (!deliveryCompanyIdStr.isEmpty()) {
                    Long deliveryCompanyId = Long.parseLong(deliveryCompanyIdStr);
                    order.getShipment().setDeliveryCompany(deliveryCompanyRepository.getReferenceById(deliveryCompanyId));
                } else {
                    order.getShipment().setDeliveryCompany(null);
                }
            }

            orderRepository.saveAndFlush(order);
        }
    }

}
