package com.siit.finalproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class OrderDto {
    private Long id;

    @NotEmpty(message = "Name cannot be null!")
    private String name;

    @NotNull(message = "Date cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy")
    //@DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "Date cannot be earlier than today")
    private LocalDate date;
}
