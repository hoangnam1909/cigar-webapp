package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Long countProductsOnSale();

    ProductResponse getProduct(Long id);

    Page<ProductResponse> getProducts(Map<String, String> params);

    List<ProductResponse> getSuggestProducts(Long id, int count);

    // ADMIN SERVICES
    Page<ProductAdminResponse> getAdminProducts(Map<String, String> params);

    ProductResponse add(ProductRequest request);

    ProductResponse update(Long id, ProductUpdateRequest request);

    ProductAdminResponse partialUpdateProduct(Long id, Map<String, Object> params);

    void delete(Long id);
}
