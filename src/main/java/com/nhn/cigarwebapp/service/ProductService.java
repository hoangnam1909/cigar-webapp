package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductResponse> getProducts(Integer page, Integer size);

    void addProduct(ProductRequest request);

}
