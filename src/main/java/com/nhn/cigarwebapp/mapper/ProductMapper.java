package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final CategoryRepository categoryRepository;
    private final AttributeValueMapper attributeValueMapper;
    private final ProductImageMapper productImageMapper;

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
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .salePrice(product.getSalePrice())
                .unitsInStock(product.getUnitsInStock())
                .category(product.getCategory())
                .brand(brandMapper.toResponse(product.getBrand()))
                .createdDate(product.getCreatedDate())
                .productImages(product.getProductImages()
                        .stream()
                        .map(productImageMapper::toResponse)
                        .toList())
//                .attributes(product.getAttributes() != null ?
//                        product.getAttributes()
//                                .stream()
//                                .map(value -> attributeValueMapper.toProductResponse(value))
//                                .collect(Collectors.toList())
//                        : null)
                .build();
    }

    public ProductAdminResponse toAdminResponse(Product product) {
        return ProductAdminResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .salePrice(product.getSalePrice())
                .unitsInStock(product.getUnitsInStock())
                .category(product.getCategory())
                .brand(brandMapper.toResponse(product.getBrand()))
                .createdDate(product.getCreatedDate())
                .modifiedDate(product.getModifiedDate())
                .productImages(product.getProductImages())
//                .attributes(product.getAttributes() != null ?
//                        product.getAttributes()
//                                .stream()
//                                .map(value -> attributeValueMapper.toProductResponse(value))
//                                .collect(Collectors.toList())
//                        : null)
                .build();
    }

}
