package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    @Value("${product.default-page-size}")
    private int PAGE_SIZE;

    private final ProductMapper productMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final SpecificationConverter specificationConverter;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", ProductSortEnum.NEWEST);

        ProductSpecification specification = specificationConverter.productSpecification(params);
        specification.add(new SearchCriteria(ProductEnum.IS_ACTIVE, true, SearchOperation.IS_ACTIVE));

        Page<ProductResponse> products = productService.getProducts(specification, page, size, sort);

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
