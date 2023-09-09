package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.BrandRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.model.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandMapper {

    public Brand toEntity(BrandRequest request) {
        return Brand.builder()
                .name(request.name())
                .description(request.description())
                .country(request.country())
                .build();
    }

    public BrandResponse toResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .image(brand.getImage())
                .country(brand.getCountry())
                .build();
    }

    public BrandWithProductsResponse toResponseWithProduct(Brand brand, List<ProductResponse> productResponseList) {
        return BrandWithProductsResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .image(brand.getImage())
                .country(brand.getCountry())
                .products(productResponseList)
                .build();
    }

    public BrandDetailResponse toDetailResponse(Brand brand) {
        return new BrandDetailResponse(
                brand.getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getImage(),
                brand.getCountry()
        );
    }

}
