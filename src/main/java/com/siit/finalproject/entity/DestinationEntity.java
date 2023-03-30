package com.siit.finalproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity(name = "destination")
public class DestinationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int distance;

    @OneToMany(mappedBy = "destination", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderEntity> orders;
}
