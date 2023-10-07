package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.product.CartProductResponse;
import com.nhn.cigarwebapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(@RequestParam(value = "ids") Set<Long> ids) {
        List<CartProductResponse> responses = cartService.getProductsInCart(ids);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(responses)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProducts(@PathVariable Long id) {
        CartProductResponse response = cartService.getProduct(id);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Product found")
                        .result(response)
                        .build());
    }

}
