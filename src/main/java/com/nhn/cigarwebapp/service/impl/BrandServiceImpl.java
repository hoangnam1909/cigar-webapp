package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.BrandMapper;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.entity.Brand;
import com.nhn.cigarwebapp.model.request.brand.BrandRequest;
import com.nhn.cigarwebapp.model.response.brand.BrandDetailResponse;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if (brand.isPresent())
            return brandMapper.toDetailResponse(brand.get());

        return null;
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
                .collect(Collectors.toList());
    }

    @Override
    public void addBrand(BrandRequest request) {
        Brand brand = brandMapper.toEntity(request);
        brandRepository.saveAndFlush(brand);
    }


}
