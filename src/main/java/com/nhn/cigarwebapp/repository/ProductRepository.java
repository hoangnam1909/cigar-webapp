package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.entity.Brand;
import com.nhn.cigarwebapp.entity.Category;
import com.nhn.cigarwebapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Long countAllByActiveIsTrue();

//    List<Product> findAllByBrandId(Long id);

    Page<Product> findAllByBrandId(Long id, Pageable pageable);

    List<Product> findAllByIdIsNotIn(List<Long> id);

    Long countProductByBrand(Brand brand);

    Long countProductByCategory(Category category);

}
