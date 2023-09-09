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
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SpecificationConverter specificationConverter;

    @Override
    public Long countProductsOnSale() {
        return productRepository.countAllByActiveIsTrue();
    }

    @Override
    @Cacheable(key = "#id", value = "Products")
    public ProductResponse getProduct(Long id) {
        System.err.println("get getProductById From DB");
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return productMapper.toResponse(product);
        }

        return null;
    }

    @Override
    @Cacheable(key = "#params", value = "Products")
    public Page<ProductResponse> getProducts(Map<String, String> params) {
        System.err.println("getProducts");

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;
        String sort = params.getOrDefault("sort", ProductSortEnum.NEWEST);

        ProductSpecification specification = specificationConverter.productSpecification(params);
        specification.add(new SearchCriteria(ProductEnum.IS_ACTIVE, true, SearchOperation.IS_ACTIVE));

        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getProductSort(sort));
        return productRepository.findAll(specification, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductAdminResponse> getAdminProducts(ProductSpecification specification, Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, sortMapper.getProductSort(sort));
        return productRepository.findAll(specification, pageable)
                .map(productMapper::toAdminResponse);
    }

    @Override
    public List<ProductResponse> getSuggestProducts(Long id, int count) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Product> products = entityManager
                    .createQuery(
                            "SELECT p " +
                                    "FROM Product p " +
                                    "WHERE (p.brand.id = :brandId OR p.category.id = :categoryId) AND p.id != :productId " +
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

    @Override
    @Transactional
    public Product add(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product productSaved = productRepository.saveAndFlush(product);

        request.getProductImagesLink()
                .forEach(link -> productImageRepository
                        .save(ProductImage.builder()
                                .linkToImage(link)
                                .product(product)
                                .build()));

        entityManager.refresh(entityManager.find(Product.class, productSaved.getId()));
        return productSaved;
    }

    @Override
    @Transactional
    @CachePut(key = "#id", value = "Product")
    public Product update(Long id, ProductUpdateRequest request) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product p = product.get();
            productRepository.save(productMapper.toEntity(request, p));
            productImageRepository.deleteAllInBatch(p.getProductImages());

            request.getProductImages().forEach(s ->
                    productImageRepository
                            .save(ProductImage.builder()
                                    .linkToImage(s)
                                    .product(p)
                                    .build())
            );

            entityManager.flush();
            return p;
        }

        return null;

    }

    @Override
    @Transactional
    @CacheEvict(key = "#id", value = "Product")
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
