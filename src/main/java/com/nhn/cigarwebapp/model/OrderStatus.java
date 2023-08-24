package com.nhn.cigarwebapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_status")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

//    @JsonIgnore
//    @JsonManagedReference
//    @OneToMany(mappedBy = "orderStatus", fetch = FetchType.LAZY)
//    private List<Order> orders;

}
