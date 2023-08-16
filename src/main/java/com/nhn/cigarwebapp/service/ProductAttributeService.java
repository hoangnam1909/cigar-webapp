package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.ProductAttributeRequest;
import com.nhn.cigarwebapp.dto.response.ProductAttributeResponse;

import java.util.List;

public interface ProductAttributeService {

    List<ProductAttributeResponse> getProductAttributes();

    void addProductAttribute(ProductAttributeRequest request);

}
