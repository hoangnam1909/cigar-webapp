package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>,
        PagingAndSortingRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
}
