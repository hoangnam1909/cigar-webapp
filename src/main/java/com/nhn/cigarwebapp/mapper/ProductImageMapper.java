package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.ProductImageResponse;
import com.nhn.cigarwebapp.model.ProductImage;
import org.springframework.stereotype.Service;

@Service
public class ProductImageMapper {

    public ProductImageResponse toResponse(ProductImage productImage){
        return ProductImageResponse.builder()
                .id(productImage.getId())
                .linkToImage(productImage.getLinkToImage())
                .build();
    }

}
