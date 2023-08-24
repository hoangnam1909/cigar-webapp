package com.nhn.cigarwebapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phone;

    @Column
    private String email;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Order> order;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Address> addresses;

}
