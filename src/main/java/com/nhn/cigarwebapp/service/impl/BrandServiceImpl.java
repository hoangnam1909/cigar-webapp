package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.mapper.BrandMapper;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.BrandService;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.brand.BrandSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BrandMapper brandMapper;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final SpecificationMapper specificationMapper;

    @Override
    @Cacheable(key = "#id", value = "brands")
    public BrandResponse getBrand(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return brand.map(brandMapper::toResponse).orElse(null);
    }

    @Override
    @Cacheable(key = "#id + '_' + #page + '_' + #size", value = "brands")
    public Page<ProductResponse> getProductOfBrand(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (brandRepository.existsById(id))
            return productRepository.findAllByBrandId(id, pageable)
                    .map(productMapper::toResponse);

        return null;
    }

    @Override
    @Cacheable(value = "brands")
    public List<BrandResponse> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toResponse)
                .sorted(Comparator.comparing(BrandResponse::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "adminBrands")
    public List<BrandAdminResponse> getAdminBrands(Map<String, String> params) {
        BrandSpecification specification = specificationMapper.brandSpecification(params);
        return brandRepository.findAll(specification)
                .stream()
                .map(brandMapper::toAdminResponse)
                .sorted(Comparator.comparing(BrandAdminResponse::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#top", value = "topBrands")
    public List<BrandWithProductsResponse> getTop(int top) {
        List<Brand> brands = entityManager
                .createQuery(
                        "SELECT b " +
                                "FROM Brand b " +
                                "ORDER BY isBestSeller DESC", Brand.class)
                .setMaxResults(top)
                .getResultList();

        return brands.stream()
                .map(brand -> {
                    Pageable pageable = PageRequest.of(0, 6);
                    List<ProductResponse> productsResponses = productRepository.findAllByBrandId(brand.getId(), pageable)
                            .getContent()
                            .stream().map(productMapper::toResponse)
                            .toList();
                    return brandMapper.toResponseWithProduct(brand, productsResponses);
                })
                .toList();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "topBrands", allEntries = true),
            @CacheEvict(value = "brands", allEntries = true),
            @CacheEvict(value = "adminBrands", allEntries = true),
    })
    public void addBrand(BrandCreationRequest request) {
        Brand brand = brandMapper.toEntity(request);
        brandRepository.saveAndFlush(brand);
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(key = "#id", value = "brands")
    }, evict = {
            @CacheEvict(value = "topBrands", allEntries = true),
            @CacheEvict(value = "brands", allEntries = true),
            @CacheEvict(value = "adminBrands", allEntries = true),
    })
    public BrandResponse update(Long id, BrandUpdateRequest request) {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandMapper.toEntity(request);
            brand.setId(id);
            brandRepository.save(brand);
            return brandMapper.toResponse(brand);
        }

        return null;
    }

}
