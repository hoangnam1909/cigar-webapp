package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.Product;
import com.nhn.cigarwebapp.repository.BrandRepository;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public void beforeAll() {
        categoryRepository.save(Category.builder()
                .name("Category 1")
                .build());
        categoryRepository.save(Category.builder()
                .name("Category 2")
                .build());

        brandRepository.save(Brand.builder()
                .name("Brand 1")
                .build());
        brandRepository.save(Brand.builder()
                .name("Brand 2")
                .build());
    }

    @Test
    void test_addProduct() {
        String productName = "Product 1 name";

        Product product = Product.builder()
                .name("Product 1")
                .category(categoryRepository.getReferenceById(1L))
                .brand(brandRepository.getReferenceById(1L))
                .name(productName)
                .originalPrice(200)
                .salePrice(100)
                .unitsInStock(10)
                .build();

        Product productSaved = productRepository.save(product);

        Assertions.assertThat(productSaved).isNotNull();
        Assertions.assertThat(productSaved.getId()).isGreaterThan(0);
        Assertions.assertThat(productRepository.findById(productSaved.getId()).get().getUnitsInStock()).isEqualTo(10);
    }
//
//    @Test
//    void getProduct() {
//    }

    @Test
    void getProducts() {
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products.size()).isGreaterThan(0);
    }

//    @Test
//    void getSuggestProducts() {
//    }
//
//    @Test
//    void getAdminProduct() {
//    }
//
//    @Test
//    void getAdminProducts() {
//    }
//
//    @Test
//    void add() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void partialUpdateProduct() {
//    }
//
//    @Test
//    void delete() {
//    }

}