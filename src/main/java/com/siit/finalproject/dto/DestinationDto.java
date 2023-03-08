package com.siit.finalproject.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationDto
{
    private long id;

    @NotEmpty(message = "Name cannot be null!")
    private String name;

    @Min(value = 1, message = "Distance must be higher than 0")
    private int distance;
}
