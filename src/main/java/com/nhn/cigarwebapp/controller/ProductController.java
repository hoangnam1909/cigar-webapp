package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.product.ProductResponse;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.ProductSpecification;
import com.nhn.cigarwebapp.specification.SortMapper;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    @Value("${product.default-page-size}")
    private int PAGE_SIZE;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationConverter specificationConverter;

    @Autowired
    private SortMapper sortMapper;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(@RequestParam Map<String, String> params) {

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;

        ProductSpecification specification = specificationConverter.productSpecification(params);

        Pageable pageable;
        if (params.containsKey("sort"))
            pageable = PageRequest.of(page - 1, size, sortMapper.getSort(params));
        else
            pageable = PageRequest.of(page - 1, size);

        Page<ProductResponse> products = productService.getProducts(specification, pageable);

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(products)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProducts(@PathVariable(name = "id") Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product found with id = " + id)
                            .result(productMapper.toResponse(product.get()))
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product not found")
                            .result(null)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProducts(@RequestBody ProductRequest request) {
        try {
            Product addedProduct = productService.add(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your products have been saved")
                            .result(productMapper.toResponse(addedProduct))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your products")
                            .result(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable(name = "id") String id) {
        try {
            productService.delete(Long.valueOf(id));

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg(String.format("Product with id = %s have been deleted", id))
                            .result("")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not delete your product")
                            .result(e.getMessage())
                            .build());
        }
    }

}
