package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.enumeration.City;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Data
public class SalonSearchDTO {

    @Enumerated(EnumType.STRING)
    private City city;

    private Long serviceId;

    private LocalDate date;

    private String time;

    @AssertTrue(message = "Time and date are required together.")
    private boolean isDateTimeCorrect() {
        return (date != null) == (time != null);
    }
}
