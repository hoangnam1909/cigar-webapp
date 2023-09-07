package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.service.OrderService;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import com.nhn.cigarwebapp.specification.sort.OrderSortEnum;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/orders")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderController {

    @Value("${order.default-page-size}")
    private int PAGE_SIZE;

    private final OrderService orderService;
    private final SpecificationConverter specificationConverter;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrders(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", OrderSortEnum.CREATED_AT_DESC);

        OrderSpecification specification = specificationConverter.orderSpecification(params);

        Page<OrderAdminResponse> orders = orderService.getAdminOrders(specification, page, size, sort);
        if (!orders.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Orders found")
                            .result(orders)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No orders")
                            .result(null)
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
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No order with id = " + id)
                            .result(null)
                            .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> partialUpdateOrder(@PathVariable String id,
                                                             @RequestBody Map<String, Object> params) {
        try {
            orderService.partialUpdateOrder(Long.valueOf(id), params);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order have been updated")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Could not update your order")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
