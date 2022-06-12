package com.example.barbershopproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Choose date.")
    private LocalDate date;

    @NotNull(message = "Choose time.")
    private String time;

    @NotNull(message = "Choose a barber.")
    private Long employeeId;

    @NotNull(message = "Choose service.")
    private Long salonServiceId;

    @NotNull(message = "Salon required.")
    private Long salonId;

    private LocalDateTime endDateTime;

    @Builder.Default
    private boolean isFinished = false;

    private String customerUsername;

    //Fields needed for view
    private String salonName;

    @Builder.Default
    private boolean canBeCancelled = false;

    private String employeeFullName;
    private String customerFullName;
    private LocalDateTime startDateTime;


}
