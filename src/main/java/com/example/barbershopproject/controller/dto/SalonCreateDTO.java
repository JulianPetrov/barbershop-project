package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.BService;
import com.example.barbershopproject.model.enumeration.City;
import com.example.barbershopproject.model.enumeration.Weekday;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SalonCreateDTO {

    private Long id;

    @NotNull(message = "Name required.")
    private String name;

    @NotNull(message = "Address required.")
    private String address;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "City required.")
    private City city;

    @NotNull(message = "Start time required.")
    private String startTime;

    @NotNull(message = "End time required.")
    private String endTime;

    @NotNull(message = "At least one workday is required.")
    private List<DayOfWeek> workdays;

    private List<MultipartFile> images;

}
