package com.nhn.cigarwebapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    @NotNull
    private Integer originalPrice;

    @Column
    @NotNull
    private Integer salePrice;

    @Column
    @NotNull
    private Integer unitsInStock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @JsonBackReference
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductImage> productImages;

//    @JsonIgnore
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private List<Comment> comments;

//    @JsonIgnore
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private List<AttributeValue> attributes;

    @PrePersist
    void prePersist() {
        this.createdDate = new Date();
        this.active = true;
    }

    @PostUpdate
    void postUpdate() {
        this.modifiedDate = new Date();
    }

}
