package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByBrandId(Long id);

    Long countProductByBrand(Brand brand);

    Long countProductByCategory(Category category);

}
