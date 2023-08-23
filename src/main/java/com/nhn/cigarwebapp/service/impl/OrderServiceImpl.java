package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.mapper.CustomerMapper;
import com.nhn.cigarwebapp.model.Customer;
import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.model.OrderItem;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.CustomerRepository;
import com.nhn.cigarwebapp.repository.OrderItemRepository;
import com.nhn.cigarwebapp.repository.OrderRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order addOrder(OrderRequest request) {
        Optional<Customer> customer = customerRepository.findByPhone(request.getPhone());
        Customer currentCustomer;

        if (customer.isPresent()) {
            currentCustomer = customer.get();
        } else {
            currentCustomer = customerMapper.toEntity(request);
            customerRepository.saveAndFlush(currentCustomer);
        }

        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        request.getOrderItems()
                .forEach(orderItemRequest -> {
                    Optional<Product> product = productRepository.findById(orderItemRequest.getProductId());
                    product.ifPresent(value -> total.updateAndGet(v -> v + value.getSalePrice() * orderItemRequest.getQuantity()));
                });

        Order order = Order.builder()
                .customer(customerRepository.getReferenceById(currentCustomer.getId()))
                .total(total.get())
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

}
