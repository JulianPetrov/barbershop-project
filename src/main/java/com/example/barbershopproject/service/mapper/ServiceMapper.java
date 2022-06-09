package com.example.barbershopproject.service.mapper;

import com.example.barbershopproject.controller.dto.ServiceDTO;
import com.example.barbershopproject.model.BService;
import com.example.barbershopproject.model.SalonServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper extends EntityMapper<ServiceDTO, BService> {

    @Mapping(target = "name", source = "service.name")
    @Mapping(target = "durationMinutes", source = "service.durationMinutes")
    @Mapping(target = "salonId", source = "salonServiceEntity.salon.id")
    @Mapping(target = "serviceId", source = "salonServiceEntity.service.id")
    @Mapping(target = "salonServiceId", source = "salonServiceEntity.id")
    @Mapping(target = "price", source = "salonServiceEntity.price")
    ServiceDTO toDto(BService service, SalonServiceEntity salonServiceEntity);
}
