package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.order.OrderItemRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderWithPaymentRequest;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.dto.response.order.OrderResponse;
import com.nhn.cigarwebapp.entity.*;
import com.nhn.cigarwebapp.mapper.CustomerMapper;
import com.nhn.cigarwebapp.mapper.OrderMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.repository.*;
import com.nhn.cigarwebapp.service.*;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import com.nhn.cigarwebapp.specification.sort.OrderSortEnum;
import com.nhn.cigarwebapp.utils.MyStringUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${order.default-page-size}")
    private int PAGE_SIZE;

    private final EntityManager entityManager;

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
    private final CustomerService customerService;
    private final EmailService emailService;
    private final MomoService momoService;
    private final PaymentRepository paymentRepository;
    private final PaymentDestinationRepository paymentDestinationRepository;

    @Override
//    @Cacheable(key = "#params", value = "orders")
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
    @Cacheable(key = "#params", value = "Page<OrderAdminResponse>")
    public Page<OrderAdminResponse> getAdminOrders(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", OrderSortEnum.CREATED_AT_DESC);

        OrderSpecification specification = specificationMapper.orderSpecification(params);
        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getOrderSort(sort));

        return orderRepository
                .findAll(specification, pageable)
                .map(orderMapper::toAdminResponse);
    }

    @Override
    @Cacheable(key = "#id", value = "OrderAdminResponse")
    public OrderAdminResponse getAdminOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.map(orderMapper::toAdminResponse).orElse(null);
    }

    @Override
    public boolean checkProductsIsInStock(List<OrderItemRequest> orderItems) {
        // order item not valid
        List<OrderItemRequest> result = orderItems
                .stream()
                .filter(orderItem -> {
                    Optional<Product> productOptional = productRepository.findById(orderItem.getProductId());
                    return orderItem.getQuantity() <= 0 ||
                            productOptional.get().getUnitsInStock() < orderItem.getQuantity() ||
                            !productOptional.get().getActive();
                })
                .toList();

        return result.isEmpty();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "List<CartProductResponse>", allEntries = true),
            @CacheEvict(value = "OrderAdminResponse", allEntries = true),
            @CacheEvict(value = "Page<OrderAdminResponse>", allEntries = true),

            @CacheEvict(value = "ProductResponse", allEntries = true),
            @CacheEvict(value = "ProductAdminResponse", allEntries = true),
            @CacheEvict(value = "Page<ProductResponse>", allEntries = true),
            @CacheEvict(value = "List<ProductSuggestResponse>", allEntries = true),
            @CacheEvict(value = "Page<ProductAdminResponse>", allEntries = true),
    })
    public Order addOrder(OrderWithPaymentRequest request) {
        Optional<Customer> customerOptional = customerRepository
                .findByFullNameAndPhone(MyStringUtils.normalizeFullName(request.getFullName()),
                        StringUtils.normalizeSpace(request.getPhone()));
        Customer customer;

        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
        } else {
            customer = customerMapper.toEntity(request);
            customerService.addCustomer(customer);
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
                .shipment(shipmentService.add(request.getDeliveryAddress()))
                .totalPrice(total.get())
                .note(request.getNote())
                .build();
        orderRepository.saveAndFlush(order);

        request.getOrderItems()
                .forEach(orderItemRequest -> {
                            Optional<Product> productOptional = productRepository.findById(orderItemRequest.getProductId());
                            if (productOptional.isPresent()) {
                                Product product = productOptional.get();
                                orderItemRepository
                                        .saveAndFlush(OrderItem.builder()
                                                .order(order)
                                                .product(productRepository.getReferenceById(product.getId()))
                                                .quantity(orderItemRequest.getQuantity())
                                                .build());

                                int newQuantity = product.getUnitsInStock() - orderItemRequest.getQuantity();
                                product.setUnitsInStock(Math.max(newQuantity, 0));
                                productRepository.save(product);
                            }
                        }
                );

        entityManager.refresh(order);
        emailService.sendOrderConfirmationEmail(order);

        if (request.getPaymentDestinationId().equalsIgnoreCase("cod")) {
            Payment payment = Payment.builder()
                    .paidAmount(order.getTotalPrice().longValue())
                    .isPaid(false)
                    .paymentUrl(null)
                    .paymentDestination(paymentDestinationRepository.getReferenceById(request.getPaymentDestinationId()))
                    .order(orderRepository.getReferenceById(order.getId()))
                    .build();
            paymentRepository.save(payment);
            entityManager.refresh(order);
        } else if (request.getPaymentDestinationId().equalsIgnoreCase("momo")) {
            Map momoCreateOrderResponse = momoService.createOrder(order);
            Payment payment = Payment.builder()
                    .paidAmount(order.getTotalPrice().longValue())
                    .isPaid(false)
                    .requestId((String) momoCreateOrderResponse.get("requestId"))
                    .paymentOrderId((String) momoCreateOrderResponse.get("orderId"))
                    .paymentUrl((String) momoCreateOrderResponse.get("payUrl"))
                    .paymentDestination(paymentDestinationRepository.getReferenceById(request.getPaymentDestinationId()))
                    .order(orderRepository.getReferenceById(order.getId()))
                    .build();

            paymentRepository.save(payment);
            entityManager.refresh(order);
        }

        return order;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "OrderAdminResponse", allEntries = true),
            @CacheEvict(value = "Page<OrderAdminResponse>", allEntries = true),
    })
    public Order partialUpdateOrder(Long id, Map<String, Object> params) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (params.containsKey("orderStatusId"))
                order.setOrderStatus(orderStatusRepository
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
            return order;
        }

        return null;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "OrderAdminResponse", allEntries = true),
            @CacheEvict(value = "Page<OrderAdminResponse>", allEntries = true),
    })
    public void deleteOrder(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
