package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        PagingAndSortingRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
