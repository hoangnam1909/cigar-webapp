package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.order.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.order.OrderStatusResponse;
import com.nhn.cigarwebapp.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/order-statuses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderStatusController {

    Logger logger = LoggerFactory.getLogger(AdminOrderStatusController.class);

    private final OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrderStatuses() {
        List<OrderStatusResponse> orderStatusesResponses = orderStatusService.getOrderStatuses();
        if (orderStatusesResponses.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order statuses found")
                            .result(orderStatusesResponses)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addOrderStatuses(@RequestBody OrderStatusRequest request) {
        try {
            OrderStatusResponse response = orderStatusService.add(request);
            if (response != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Order status have been saved")
                                .result(null)
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Could not save your order status")
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

//    @PostMapping
//    public ResponseEntity<ResponseObject> addOrderStatuses(@RequestBody List<OrderStatusRequest> requestList) {
//        try {
//            orderStatusService.add(requestList);
//            return ResponseEntity.ok()
//                    .body(ResponseObject.builder()
//                            .msg("Order statuses have been saved")
//                            .result(null)
//                            .build());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.badRequest()
//                    .body(ResponseObject.builder()
//                            .msg("Could not save your order statuses")
//                            .result(ex.getMessage())
//                            .build());
//        }
//    }

}
