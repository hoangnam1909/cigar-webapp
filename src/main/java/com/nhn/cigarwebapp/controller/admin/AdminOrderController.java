package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.admin.AdminOrderCreationRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderWithPaymentRequest;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/orders")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderController {

    Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrders(@RequestParam Map<String, String> params) {
        Page<OrderAdminResponse> orders = orderService.getAdminOrders(params);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Orders found")
                        .result(orders)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrder(@PathVariable String id) {
        OrderAdminResponse orderResponse = orderService.getAdminOrder(Long.valueOf(id));
        if (orderResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order found with id = " + id)
                            .result(orderResponse)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No order with id = " + id)
                            .result(null)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addOrder(@RequestBody AdminOrderCreationRequest request) {
        try {
            if (orderService.checkProductsIsValidForOrder(request.getOrderItems())) {
                Order order = orderService.adminAddOrder(request);
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your order have been saved")
                                .result(order)
                                .build());
            } else {
                throw new IllegalArgumentException("Product items is invalid");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result(ex.getMessage())
                            .build());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> partialUpdateOrder(@PathVariable String id,
                                                             @RequestBody Map<String, Object> params) {
        try {
            Order order = orderService.partialUpdateOrder(Long.valueOf(id), params);
            if (order != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Order have been updated")
                                .result(order)
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Could not update your order")
                                .result(null)
                                .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order have been deleted")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

}
