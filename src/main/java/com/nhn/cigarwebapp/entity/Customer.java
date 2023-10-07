package com.nhn.cigarwebapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String address;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Order> order;

    @PrePersist
    @PreUpdate
    void normalizeFullName() {
        fullName = StringUtils.normalizeSpace(fullName);
        fullName = WordUtils.capitalize(fullName);
    }

}
