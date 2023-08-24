package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Value("${order.default-page-size}")
    private int PAGE_SIZE;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrders(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;

        Page<OrderResponse> orderResponses = orderService.getOrders(page - 1, size);
        if (orderResponses.getContent().isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(List.of())
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Orders found")
                            .result(orderResponses)
                            .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrder(@PathVariable String id) {
        OrderResponse orderResponse = orderService.getOrder(Long.valueOf(id));
        if (orderResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order found with id = " + id)
                            .result(orderResponse)
                            .build());
        else
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("No order with id = " + id)
                            .result(null)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProducts(@RequestBody OrderRequest request) {
        try {
            Order order = orderService.addOrder(request);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your order have been saved")
                            .result(order)
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your order")
                            .result(ex.getMessage())
                            .build());
        }

    }

}
