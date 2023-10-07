package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.product.CartProductResponse;
import com.nhn.cigarwebapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartMapper {

    public CartProductResponse toResponse(Product product) {
        return CartProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .image(product.getProductImages().get(0).getLinkToImage())
                .originalPrice(product.getOriginalPrice())
                .salePrice(product.getSalePrice())
                .unitsInStock(product.getUnitsInStock())
                .build();
    }

}
