package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "${product.default-page-size}") String size) {

        Page<ProductResponse> products = productService
                .getProducts(Integer.parseInt(page) - 1, Integer.parseInt(size));

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(products)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProducts(@RequestBody ProductRequest request) {
        try {
            productService.addProduct(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your products have been saved")
                            .result("")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your products")
                            .result(e.getMessage())
                            .build());
        }
    }

}
