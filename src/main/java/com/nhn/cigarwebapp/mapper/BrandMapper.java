package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.entity.Brand;
import com.nhn.cigarwebapp.model.request.brand.BrandRequest;
import com.nhn.cigarwebapp.model.response.brand.BrandDetailResponse;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import org.springframework.stereotype.Service;

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
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getImage(),
                brand.getCountry()
        );
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
