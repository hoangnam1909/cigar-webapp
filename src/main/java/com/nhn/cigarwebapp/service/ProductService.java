package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.product.ProductResponse;
import com.nhn.cigarwebapp.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> getProducts(Integer page, Integer size);

    Page<ProductResponse> getProducts(ProductSpecification specification, Pageable pageable);

    Product add(ProductRequest request);

    void delete(Long id);

}
