package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.model.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandMapper {

    public Brand toEntity(BrandCreationRequest request) {
        return Brand.builder()
                .name(request.getName())
                .link(request.getLink())
                .description(request.getDescription())
                .image(request.getImage())
                .country(request.getCountry())
                .isBestSeller(request.getIsBestSeller())
                .build();
    }

    public Brand toEntity(BrandUpdateRequest request) {
        return Brand.builder()
                .name(request.getName())
                .link(request.getLink())
                .description(request.getDescription())
                .image(request.getImage())
                .country(request.getCountry())
                .isBestSeller(request.getIsBestSeller())
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

    public BrandAdminResponse toAdminResponse(Brand brand) {
        return BrandAdminResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .link(brand.getLink())
                .description(brand.getDescription())
                .image(brand.getImage())
                .country(brand.getCountry())
                .isBestSeller(brand.getIsBestSeller())
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

}
