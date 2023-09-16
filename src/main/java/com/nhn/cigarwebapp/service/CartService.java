package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.response.CartProductResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;

import java.util.List;
import java.util.Set;

public interface CartService {

    CartProductResponse getProduct(Long id);

    List<CartProductResponse> getProductsInCart(Set<Long> ids);

}
