package com.nhn.cigarwebapp.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute_value")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", referencedColumnName = "id")
    private ProductAttribute productAttribute;

    @Column
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

}
