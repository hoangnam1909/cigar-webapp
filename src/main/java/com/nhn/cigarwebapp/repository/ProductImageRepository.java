package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.model.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    boolean existsByLinkToImage(String linkToImage);

}
