package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.entity.Product;
import com.nhn.cigarwebapp.model.request.product.ProductRequest;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.repository.ProductImageRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product toEntity(ProductRequest request){
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .originalPrice(request.getOriginalPrice())
                .salePrice(request.getSalePrice())
                .unitsInStock(request.getUnitsInStock())
                .category(categoryRepository.getReferenceById(request.getCategoryId()))
                .brand(brandRepository.getReferenceById(request.getBrandId()))
                .build();
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
                product.getAttributes()
        );
    }

}
