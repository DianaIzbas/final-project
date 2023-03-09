package com.siit.finalproject.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "orders")
public class OrderEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String deliveryDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;
}
