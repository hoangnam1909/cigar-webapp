package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Long countProductsOnSale();

    Page<ProductResponse> getProducts(Integer page, Integer size);

    Page<ProductResponse> getProducts(ProductSpecification specification, Integer page, Integer size, String sort);

//    Page<ProductAdminResponse> getAdminProducts(ProductSpecification specification, Pageable pageable);

    Page<ProductAdminResponse> getAdminProducts(ProductSpecification specification, Integer page, Integer size, String sort);

    List<ProductResponse> getSuggestProducts(Long id, int count);

    Product add(ProductRequest request);

    Product update(Long id, ProductUpdateRequest request);

    void delete(Long id);

}
