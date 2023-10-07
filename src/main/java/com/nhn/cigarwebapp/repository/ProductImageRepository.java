package com.nhn.cigarwebapp.repository;

import com.nhn.cigarwebapp.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    boolean existsByLinkToImage(String linkToImage);

    ProductImage findByLinkToImageAndProductId(String linkToImage, Long id);

    List<ProductImage> findAllByProductId(Long id);

    Long deleteByLinkToImage(String linkToImage);

}
