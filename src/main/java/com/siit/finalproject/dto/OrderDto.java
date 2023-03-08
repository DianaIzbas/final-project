package com.siit.finalproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class OrderDto {


    private long id;

    @NotEmpty(message = "Name cannot be null!")
    private String name;

    @NotEmpty(message = "Date cannot be null")
    private LocalDate date;
}
