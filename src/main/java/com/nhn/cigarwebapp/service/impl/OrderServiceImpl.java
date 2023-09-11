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
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import com.nhn.cigarwebapp.specification.sort.OrderSortEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${order.default-page-size}")
    private int PAGE_SIZE;

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
    private final SpecificationMapper specificationMapper;

    @Override
    @Cacheable(key = "#params", value = "order")
    public OrderResponse getOrder(@RequestParam Map<String, String> params) {
        if (params.containsKey("orderId") && params.containsKey("phone")) {
            Optional<Order> orderOptional = orderRepository.findById(Long.valueOf(params.get("orderId")));
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (order.getCustomer().getPhone().equals(params.get("phone")))
                    return orderMapper.toResponse(order);
            }
        }

        return null;
    }

    @Override
    @Cacheable(key = "#params", value = "adminOrders")
    public Page<OrderAdminResponse> getAdminOrders(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", OrderSortEnum.CREATED_AT_DESC);

        OrderSpecification specification = specificationMapper.orderSpecification(params);
        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getOrderSort(sort));

        return orderRepository.findAll(specification, pageable)
                .map(orderMapper::toAdminResponse);
    }

    @Override
    @Cacheable(key = "#id", value = "adminOrders")
    public OrderAdminResponse getAdminOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::toAdminResponse).orElse(null);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "adminOrders", allEntries = true),
    })
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
    @Caching(evict = {
            @CacheEvict(value = "order", allEntries = true),
            @CacheEvict(value = "adminOrders", allEntries = true),
    })
    public OrderAdminResponse partialUpdateOrder(Long id, Map<String, Object> params) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (params.containsKey("orderStatusId"))
                order.setOrderStatus(
                        orderStatusRepository
                                .getReferenceById(Long.valueOf((String) params.get("orderStatusId"))));

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
            return orderMapper.toAdminResponse(order);
        }

        return null;
    }

}
