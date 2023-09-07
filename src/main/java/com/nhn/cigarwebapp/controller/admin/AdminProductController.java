package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

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
        String sort = params.getOrDefault("sort", ProductSortEnum.NEWEST);

        ProductSpecification specification = specificationConverter.productSpecification(params);
        specification.add(new SearchCriteria(ProductEnum.IS_ACTIVE, true, SearchOperation.IS_ACTIVE));

        Page<ProductAdminResponse> products = productService.getAdminProducts(specification, page, size, sort);

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(products)
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

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateEntireProducts(
            @PathVariable String id,
            @RequestBody ProductUpdateRequest request) {
        try {
            Product addedProduct = productService.update(Long.valueOf(id), request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your product have been updated")
                            .result(productMapper.toResponse(addedProduct))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not update your product")
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
