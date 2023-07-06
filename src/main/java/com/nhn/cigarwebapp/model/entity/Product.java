package com.nhn.cigarwebapp.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double originalPrice;

    @Column
    private Double salePrice;

    @Column
    private String productImage;

    @Column
    private Integer unitsInStock;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Comment> commentList;

}
