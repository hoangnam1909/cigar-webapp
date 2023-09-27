package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.model.ProductImage;
import com.nhn.cigarwebapp.repository.ProductImageRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.FileService;
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${product.default-page-size}")
    private int PAGE_SIZE;

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final SortMapper sortMapper;
    private final SpecificationMapper specificationMapper;
    private final FileService fileService;

    @Override
    @Cacheable("countProductsOnSale")
    public Long countProductsOnSale() {
        return productRepository.countAllByActiveIsTrue();
    }

    @Override
    @Cacheable(key = "#id", value = "product")
    public ProductResponse getProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getActive())
                return productMapper.toResponse(product);
        }

        return null;
    }

    @Override
    @Cacheable(key = "#params", value = "products")
    public Page<ProductResponse> getProducts(Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", ProductSortEnum.DEFAULT);

        ProductSpecification specification = specificationMapper.productSpecification(params);
        specification.add(new SearchCriteria(ProductEnum.IS_ACTIVE, true, SearchOperation.IS_ACTIVE));

        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getProductSort(sort));
        return productRepository.findAll(specification, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Cacheable(key = "{#id, #count}", value = "productSuggest")
    public List<ProductResponse> getSuggestProducts(Long id, int count) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Product> products = entityManager
                    .createQuery(
                            "SELECT p " +
                                    "FROM Product p " +
                                    "WHERE (p.brand.id = :brandId OR p.category.id = :categoryId) " +
                                    "AND p.id != :productId " +
                                    "AND p.active = true " +
                                    "ORDER BY random()", Product.class)
                    .setParameter("productId", id)
                    .setParameter("brandId", product.getBrand().getId())
                    .setParameter("categoryId", product.getCategory().getId())
                    .setMaxResults(count)
                    .getResultList();
            return products
                    .stream()
                    .map(productMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    // ADMIN SERVICES
    @Override
    @Cacheable(key = "#id", value = "adminProduct")
    public ProductAdminResponse getAdminProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return productMapper.toAdminResponse(product);
        }
        return null;
    }

    @Override
    @Cacheable(key = "#params", value = "adminProducts")
    public Page<ProductAdminResponse> getAdminProducts(Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", ProductSortEnum.ADMIN_DEFAULT);

        ProductSpecification specification = specificationMapper.productSpecification(params);

        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getProductSort(sort));

        return productRepository.findAll(specification, pageable)
                .map(productMapper::toAdminResponse);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productSuggest", allEntries = true),
            @CacheEvict(value = "adminProducts", allEntries = true),
            @CacheEvict(value = "countProductsOnSale", allEntries = true),
    })
    public ProductResponse add(ProductRequest request, List<MultipartFile> files) {
        Product product = productMapper.toEntity(request);
        Product productSaving = productRepository.saveAndFlush(product);

        List<String> links = fileService.uploadFiles(files);
        links.forEach(link -> productImageRepository
                .save(ProductImage.builder()
                        .linkToImage(link)
                        .product(product)
                        .build()));

//        request.getProductImages()
//                .forEach(link -> productImageRepository
//                        .save(ProductImage.builder()
//                                .linkToImage(link)
//                                .product(product)
//                                .build()));

        entityManager.refresh(entityManager.find(Product.class, productSaving.getId()));
        return productMapper.toResponse(productSaving);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id", value = "product"),
            @CacheEvict(key = "#id", value = "adminProduct"),
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productSuggest", allEntries = true),
            @CacheEvict(value = "adminProducts", allEntries = true),
    })
    public ProductResponse update(Long id, ProductUpdateRequest request, List<MultipartFile> files) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepository.save(productMapper.toEntity(request, product));
            productImageRepository.deleteAllInBatch(product.getProductImages());

            List<String> links = fileService.uploadFiles(files);
            links.forEach(link -> productImageRepository
                    .save(ProductImage.builder()
                            .linkToImage(link)
                            .product(product)
                            .build()));

//            request.getProductImages().forEach(s ->
//                    productImageRepository
//                            .save(ProductImage.builder()
//                                    .linkToImage(s)
//                                    .product(product)
//                                    .build())
//            );

            entityManager.flush();
            return productMapper.toResponse(product);
        }

        return null;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id", value = "product"),
            @CacheEvict(key = "#id", value = "adminProduct"),
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productSuggest", allEntries = true),
            @CacheEvict(value = "adminProducts", allEntries = true),
    })
    public void partialUpdateProduct(Long id, Map<String, Object> params) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (params.containsKey("active")) {
                product.setActive(Boolean.parseBoolean(String.valueOf(params.get("active"))));
            }
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productSuggest", allEntries = true),
            @CacheEvict(value = "adminProducts", allEntries = true),
            @CacheEvict(value = "countProductsOnSale", allEntries = true),
    })
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
