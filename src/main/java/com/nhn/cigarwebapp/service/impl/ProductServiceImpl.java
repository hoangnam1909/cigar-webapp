package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.entity.ProductImage;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;
import com.nhn.cigarwebapp.repository.ProductImageRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public Page<ProductResponse> getProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable)
                .map(product -> productMapper.toResponse(product));
    }

    @Override
    public void addProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        productRepository.saveAndFlush(product);

        request.getProductImagesLink()
                .forEach(link -> productImageRepository
                        .save(ProductImage.builder()
                                .linkToImage(link)
                                .product(product)
                                .build()));
    }

}