package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.Employee;
import com.example.barbershopproject.model.SalonServiceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class AppointmentCreateDTO {

    @NotNull(message = "Choose a barber.")
    private Employee barber;

    @NotNull(message = "Choose service.")
    private SalonServiceEntity salonServiceEntity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "Choose start time.")
    @PastOrPresent(message = "Appointment must be in the future.")
    private LocalDateTime startTime;


}
