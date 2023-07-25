package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.entity.Brand;
import com.nhn.cigarwebapp.model.request.brand.BrandRequest;
import com.nhn.cigarwebapp.model.response.brand.BrandDetailResponse;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import com.nhn.cigarwebapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandMapper {

    @Autowired
    private ProductRepository productRepository;

    public Brand toEntity(BrandRequest request) {
        return Brand.builder()
                .name(request.name())
                .description(request.description())
                .country(request.country())
                .build();
    }

    public BrandResponse toResponse(Brand brand) {
        Long productsCount = productRepository.countProductByBrand(brand);

        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getImage(),
                brand.getCountry(),
                productsCount
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
