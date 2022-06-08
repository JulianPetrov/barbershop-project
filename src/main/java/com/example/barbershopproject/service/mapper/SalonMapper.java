package com.example.barbershopproject.service.mapper;

import com.example.barbershopproject.controller.dto.SalonDTO;
import com.example.barbershopproject.model.Salon;
import com.example.barbershopproject.service.BarberServicesService;
import com.example.barbershopproject.service.FileService;
import com.example.barbershopproject.service.SalonWorkdayService;
import com.example.barbershopproject.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(
    componentModel = "spring",
    uses = {
      UserService.class,
      FileService.class,
      SalonWorkdayService.class,
      BarberServicesService.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalonMapper extends EntityMapper<SalonDTO, Salon> {

  @Mapping(source = "ownerId", target = "owner")
  Salon toEntity(SalonDTO salonDTO);

  @Mappings({
    @Mapping(source = "owner.id", target = "ownerId"),
    @Mapping(source = "id", target = "imageNames"),
    @Mapping(source = "owner.username", target = "ownerUsername"),
    @Mapping(
        target = "startTime",
        expression = "java(convertLocalTimeToString(salon.getStartTime()))"),
    @Mapping(target = "endTime", expression = "java(convertLocalTimeToString(salon.getEndTime()))"),
    @Mapping(source = "id", target = "workdays"),
    @Mapping(source = "id", target = "services")
  })
  SalonDTO toDto(Salon salon);

  default String convertLocalTimeToString(LocalTime time) {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"));
  }
}
