package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.order.OrderWithPaymentRequest;
import com.nhn.cigarwebapp.dto.response.order.OrderResponse;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/tracking")
    public ResponseEntity<ResponseObject> trackingOrder(@RequestParam Map<String, String> params) {
        OrderResponse orderResponse = orderService.getOrder(params);

        if (orderResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order found with id = " + params.get("orderId"))
                            .result(orderResponse)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No order")
                            .result(null)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addOrder(@RequestBody OrderWithPaymentRequest request) {
        try {
            if (orderService.checkProductsIsInStock(request.getOrderItems())) {
                Order order = orderService.addOrder(request);
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your order have been saved")
                                .result(order)
                                .build());
            } else {
                throw new IllegalArgumentException("Product items is invalid");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your order")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
