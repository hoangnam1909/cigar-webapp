package com.nhn.cigarwebapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonBackReference
    private Customer customer;

    @Column
    private Date createdAt;

    @Column
    private Double total;

    @Column
    private String note;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private Set<OrderItem> orderItems;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_status_id", referencedColumnName = "id")
    private OrderStatus orderStatus;

    @Column
    private String deliveryAddress;

    @PrePersist
    void prePersist() {
        this.createdAt = new Date();
    }

}