package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.ProductAttributeMapper;
import com.nhn.cigarwebapp.model.entity.ProductAttribute;
import com.nhn.cigarwebapp.model.request.productAttribute.ProductAttributeRequest;
import com.nhn.cigarwebapp.model.response.productAttribute.ProductAttributeResponse;
import com.nhn.cigarwebapp.repository.ProductAttributeRepository;
import com.nhn.cigarwebapp.service.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public List<ProductAttributeResponse> getProductAttributes() {
        return productAttributeRepository.findAll()
                .stream()
                .map(productAttribute -> productAttributeMapper.toResponse(productAttribute))
                .collect(Collectors.toList());
    }

    @Override
    public void addProductAttribute(ProductAttributeRequest request) {
        ProductAttribute productAttribute = productAttributeMapper.toEntity(request);
        productAttributeRepository.save(productAttribute);

    }

}
