package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.mapper.ProductMapper;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.model.ProductImage;
import com.nhn.cigarwebapp.repository.ProductImageRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import com.nhn.cigarwebapp.service.ProductService;
import com.nhn.cigarwebapp.specification.ProductSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countProductsOnSale() {
        return productRepository.countAllByActiveIsTrue();
    }

    @Override
    public Page<ProductResponse> getProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable)
                .map(product -> productMapper.toResponse(product));
    }

    @Override
    public Page<ProductResponse> getProducts(ProductSpecification specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable)
                .map(product -> productMapper.toResponse(product));
    }

    @Override
    public List<ProductResponse> getSuggestProducts(Long id, int count) {
        Product product = productRepository.findById(id).get();
        System.err.println("suggest");
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
                .map(p -> productMapper.toResponse(p))
                .collect(Collectors.toList());
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
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
