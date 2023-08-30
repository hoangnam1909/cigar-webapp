package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.BrandRequest;
import com.nhn.cigarwebapp.dto.request.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.mapper.BrandMapper;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public BrandDetailResponse getBrandDetail(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return brand.map(value -> brandMapper.toDetailResponse(value)).orElse(null);
    }

    @Override
    public Page<ProductResponse> getProductOfBrand(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (brandRepository.existsById(id))
            return productRepository.findAllByBrandId(id, pageable)
                    .map(p -> productMapper.toResponse(p));

        return null;
    }

    @Override
    public List<BrandResponse> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(b -> brandMapper.toResponse(b))
                .sorted(Comparator.comparing(BrandResponse::id))
                .collect(Collectors.toList());
    }

    @Override
    public List<BrandWithProductsResponse> getTop3() {
        return brandRepository.findTop3ByOrderByIsBestSellerDesc()
                .stream()
                .map(brand -> {
                    Pageable pageable = PageRequest.of(0,6);
                    List<ProductResponse> productsResponses = productRepository.findAllByBrandId(brand.getId(), pageable)
                            .getContent()
                            .stream().map(product -> productMapper.toResponse(product))
                            .toList();
                    return brandMapper.toResponseWithProduct(brand, productsResponses);
                })
                .toList();
    }

    @Override
    public void addBrand(BrandRequest request) {
        Brand brand = brandMapper.toEntity(request);
        brandRepository.saveAndFlush(brand);
    }

    @Override
    public BrandResponse update(Long id, BrandUpdateRequest request) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.isPresent()) {
            Brand brandEditing = brand.get();
            brandEditing.setName(request.name());
            brandEditing.setCountry(request.country());
            brandRepository.save(brandEditing);
            return brandMapper.toResponse(brandEditing);
        }
        return null;
    }

}
