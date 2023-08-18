package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.mapper.BrandMapper;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.dto.request.BrandRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductResponse> getProductOfBrand(Long id) {
        if (brandRepository.existsById(id)) {
            return productRepository.findAllByBrandId(id)
                    .stream()
                    .map(p -> productMapper.toResponse(p))
                    .collect(Collectors.toList());
        }

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
