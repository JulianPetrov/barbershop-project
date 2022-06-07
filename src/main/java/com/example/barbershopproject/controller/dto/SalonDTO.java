package com.example.barbershopproject.controller.dto;

import com.example.barbershopproject.model.BService;
import com.example.barbershopproject.model.enumeration.City;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SalonDTO {

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
    private List<BService> services;

    private Long ownerId;
    private String ownerUsername;
    private List<String> imageNames;

}
