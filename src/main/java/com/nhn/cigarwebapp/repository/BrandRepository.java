package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findTop3ByOrderByIsBestSellerDesc();

}
