package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>,
        PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
