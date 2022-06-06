package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.BService;
import com.example.barbershopproject.model.Salon;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ServiceAddDTO {

    @NotNull(message = "Salon required.")
    private Salon salon;

    @NotNull(message = "Service required.")
    private BService service;

    @NotNull(message = "Price required.")
    private BigDecimal price;

}
