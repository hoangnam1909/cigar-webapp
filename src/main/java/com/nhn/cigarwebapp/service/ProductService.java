package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductResponse> getProducts(Integer page, Integer size);

    Page<ProductResponse> getProducts(ProductSpecification specification, Pageable pageable);

    List<ProductResponse> getSuggestProducts(Long id);

    Product add(ProductRequest request);

    Product update(Long id, ProductUpdateRequest request);

    void delete(Long id);

}
