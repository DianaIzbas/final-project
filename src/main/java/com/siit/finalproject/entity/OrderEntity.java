package com.siit.finalproject.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.siit.finalproject.enums.OrderEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderEnum status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id")
    private DestinationEntity destination;

}
