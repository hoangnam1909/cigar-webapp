package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.request.productAttribute.ProductAttributeRequest;
import com.nhn.cigarwebapp.model.response.productAttribute.ProductAttributeResponse;

import java.util.List;

public interface ProductAttributeService {

    List<ProductAttributeResponse> getProductAttributes();

    void addProductAttribute(ProductAttributeRequest request);

}
