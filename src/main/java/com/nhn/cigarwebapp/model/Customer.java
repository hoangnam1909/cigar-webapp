package com.nhn.cigarwebapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="customer")
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

    @Column
    private String address;

    @Column
    private String note;

    @OneToOne(mappedBy = "customer")
    private Order order;

}
