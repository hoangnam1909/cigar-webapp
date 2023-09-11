package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>,
        PagingAndSortingRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
}
