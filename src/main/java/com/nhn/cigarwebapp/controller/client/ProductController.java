package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import com.nhn.cigarwebapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(@RequestParam Map<String, String> params) {
        Page<ProductResponse> products = productService.getProducts(params);

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(products)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProducts(@PathVariable(name = "id") Long id) {
        ProductResponse productResponse = productService.getProduct(id);

        if (productResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product found with id = " + id)
                            .result(productResponse)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product not found")
                            .result(null)
                            .build());
    }

    @GetMapping("/count-product-on-sale")
    public ResponseEntity<ResponseObject> countProductsOnSale() {
        try {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Successfully")
                            .result(productService.countProductsOnSale())
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Error")
                            .result(ex.getMessage())
                            .build());
        }
    }

    @GetMapping("/suggests/{id}")
    public ResponseEntity<ResponseObject> getSuggestProducts(@PathVariable(name = "id") Long id,
                                                             @RequestParam Map<String, String> params) {
        int count = params.containsKey("count") ? Integer.parseInt(params.get("count")) : 4;
        List<ProductResponse> products = productService.getSuggestProducts(id, count);

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("success")
                        .result(products)
                        .build());
    }

}
