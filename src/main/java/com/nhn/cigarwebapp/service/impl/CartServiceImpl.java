package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.dto.response.CartProductResponse;
import com.nhn.cigarwebapp.mapper.CartMapper;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.CartService;
import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public CartProductResponse getProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(cartMapper::toResponse).orElse(null);
    }

    @Override
    public List<CartProductResponse> getProductsInCart(Set<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products
                .stream()
                .filter(product -> product.getActive() && product.getUnitsInStock() > 0)
                .map(cartMapper::toResponse)
                .toList();
    }

}
