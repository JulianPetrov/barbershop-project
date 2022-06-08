package com.example.barbershopproject.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ServiceDTO {

    @NotNull(message = "Salon required.")
    private Long salonId;

    @NotNull(message = "Service required.")
    private Long serviceId;

    @NotNull(message = "Price required.")
    private BigDecimal price;

    private String name;
    private Integer durationMinutes;

}
