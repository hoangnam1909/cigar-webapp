package com.nhn.cigarwebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "delivery_company_id", referencedColumnName = "id")
    private DeliveryCompany deliveryCompany;

}
