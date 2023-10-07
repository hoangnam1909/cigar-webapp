package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.product.ProductRequest;
import com.nhn.cigarwebapp.dto.request.product.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Long countProductsOnSale();

    ProductResponse getProduct(Long id);

    Page<ProductResponse> getProducts(Map<String, String> params);

    List<ProductResponse> getSuggestProducts(Long id, int count);

    // ADMIN SERVICES
    ProductAdminResponse getAdminProduct(Long id);

    Page<ProductAdminResponse> getAdminProducts(Map<String, String> params);

    ProductResponse add(ProductRequest request, List<MultipartFile> files);

    ProductResponse update(Long id, ProductUpdateRequest request, List<MultipartFile> files);

    void partialUpdateProduct(Long id, Map<String, Object> params);

    void delete(Long id);
}
