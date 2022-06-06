package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.Salon;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeDTO {

    @NotNull(message = "First name required.")
    private String firstName;

    @NotNull(message = "Last name required.")
    private String lastName;

    @NotNull(message = "Salon required.")
    private Salon salon;

}
