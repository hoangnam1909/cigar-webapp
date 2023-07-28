package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {

    List<AttributeValue> findAllByProductId(Long productId);

}
