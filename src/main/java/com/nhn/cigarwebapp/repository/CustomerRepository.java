package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByFullNameAndPhone(String fullName, String phone);

}
