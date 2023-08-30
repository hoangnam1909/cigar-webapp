package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeValueMapper attributeValueMapper;

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription().replaceAll("\"", "'"))
                .originalPrice(request.getOriginalPrice())
                .salePrice(request.getSalePrice())
                .unitsInStock(request.getUnitsInStock())
                .category(categoryRepository.getReferenceById(request.getCategoryId()))
                .brand(brandRepository.getReferenceById(request.getBrandId()))
                .build();
    }

    public Product toEntity(ProductUpdateRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setSalePrice(request.getSalePrice());
        product.setUnitsInStock(request.getUnitsInStock());
        product.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        product.setBrand(brandRepository.getReferenceById(request.getBrandId()));

        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getOriginalPrice(),
                product.getSalePrice(),
                product.getUnitsInStock(),
                product.getCategory(),
                brandMapper.toResponse(product.getBrand()),
                product.getProductImages(),
                product.getAttributes() != null ?
                        product.getAttributes()
                                .stream()
                                .map(value -> attributeValueMapper.toProductResponse(value))
                                .collect(Collectors.toList())
                        : null
        );
    }

}
