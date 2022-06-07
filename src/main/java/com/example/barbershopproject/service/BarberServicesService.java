package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.ServiceDTO;
import com.example.barbershopproject.model.BService;
import com.example.barbershopproject.model.Salon;
import com.example.barbershopproject.model.SalonServiceEntity;
import com.example.barbershopproject.repository.BServiceRepository;
import com.example.barbershopproject.repository.SalonServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberServicesService {

    private final BServiceRepository serviceRepository;
    private final SalonServiceEntityRepository salonServiceEntityRepository;


    public List<BService> getAllServicesOfSalon(Long salonId) {
        return salonServiceEntityRepository.findAllBySalon_Id(salonId).stream()
                .map(SalonServiceEntity::getService)
                .collect(Collectors.toList());
    }

    public List<BService> getAllServices() {
        return serviceRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public void addServiceToSalon(Salon salon, ServiceDTO serviceDTO) {
        BService service = getServiceById(serviceDTO.getServiceId());
        salonServiceEntityRepository.save(
                new SalonServiceEntity(salon, service, serviceDTO.getPrice()));
    }


    public BService getServiceById(Long id) {
        return serviceRepository.findById(id).orElseThrow();
    }
}
