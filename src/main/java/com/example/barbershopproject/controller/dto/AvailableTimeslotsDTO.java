package com.example.barbershopproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AvailableTimeslotsDTO {
    private List<LocalDateTime> availableTimeslots;
}
