package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.entity.PaymentDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDestinationRepository extends
        JpaRepository<PaymentDestination, String>,
        JpaSpecificationExecutor<PaymentDestination> {
}
