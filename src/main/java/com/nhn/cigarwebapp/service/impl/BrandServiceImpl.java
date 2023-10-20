package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.dto.response.brand.BrandResponse;
import com.nhn.cigarwebapp.dto.response.brand.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import com.nhn.cigarwebapp.entity.Brand;
import com.nhn.cigarwebapp.mapper.BrandMapper;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.BrandService;
import com.nhn.cigarwebapp.service.ProductService;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final ProductService productService;

    @Override
    @Cacheable(key = "#id", value = "BrandResponse")
    public BrandResponse getBrand(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::toResponse)
                .orElse(null);
    }

    @Override
    @Cacheable(key = "#id + '_' + #page + '_' + #size", value = "Page<ProductResponse>")
    public Page<ProductResponse> getProductOfBrand(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (brandRepository.existsById(id))
            return productRepository.findAllByBrandId(id, pageable)
                    .map(productMapper::toResponse);

        return null;
    }

    @Override
    @Cacheable(value = "List<BrandResponse>")
    public List<BrandResponse> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toResponse)
                .sorted(Comparator.comparing(BrandResponse::getId))
                .toList();
    }

    @Override
    @Cacheable(key = "#id", value = "BrandAdminResponse")
    public BrandAdminResponse getAdminBrand(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::toAdminResponse)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "Page<BrandAdminResponse>")
    public Page<BrandAdminResponse> getAdminBrands(Map<String, String> params) {
        int pageSize = 15;

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : pageSize;

        BrandSpecification specification = specificationMapper.brandSpecification(params);
        Pageable pageable;
        if (page == 0)
            pageable = Pageable.unpaged();
        else
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("id")));

        return brandRepository.findAll(specification, pageable)
                .map(brandMapper::toAdminResponse);
    }

    @Override
    @Cacheable(key = "#top", value = "List<BrandWithProductsResponse>")
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
                    Map<String, String> params = new HashMap<>();
                    params.put("size", "6");
                    params.put("brandId", brand.getId().toString());
                    return brandMapper.toResponseWithProduct(brand,
                            productService.getProducts(params).getContent());
                })
                .toList();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "List<BrandResponse>", allEntries = true),
            @CacheEvict(value = "Page<BrandAdminResponse>", allEntries = true),
            @CacheEvict(value = "List<BrandWithProductsResponse>", allEntries = true),
    })
    public void addBrand(BrandCreationRequest request) {
        Brand brand = brandMapper.toEntity(request);
        brandRepository.saveAndFlush(brand);
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(key = "#id", value = "BrandResponse")
    }, evict = {
            @CacheEvict(key = "#id", value = "BrandAdminResponse"),
            @CacheEvict(value = "List<BrandResponse>", allEntries = true),
            @CacheEvict(value = "Page<BrandAdminResponse>", allEntries = true),
            @CacheEvict(value = "List<BrandWithProductsResponse>", allEntries = true),
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
