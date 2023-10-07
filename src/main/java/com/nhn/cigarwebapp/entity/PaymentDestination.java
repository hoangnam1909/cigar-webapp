package com.nhn.cigarwebapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "payment-destination")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDestination implements Serializable {

    @Id
    private String id;

    @Column
    private String logo;

    @Column
    private String shortName;

    @Column
    private String name;

    @Column
    private Boolean active;

    @Column
    private Integer sortIndex;

}
