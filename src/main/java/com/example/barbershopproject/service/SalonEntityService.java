package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.EmployeeDTO;
import com.example.barbershopproject.controller.dto.SalonDTO;
import com.example.barbershopproject.controller.dto.ServiceDTO;
import com.example.barbershopproject.model.*;
import com.example.barbershopproject.model.enumeration.City;
import com.example.barbershopproject.model.enumeration.CityItem;
import com.example.barbershopproject.repository.*;
import com.example.barbershopproject.service.mapper.SalonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonEntityService {

  private final SalonRepository salonRepository;
  private final SalonWorkdayRepository salonWorkdayRepository;
  private final EmployeeRepository employeeRepository;
  private final UserService userService;
  private final FileService fileService;
  private final BarberServicesService barberServicesService;
  private final SalonMapper salonMapper;

  @Transactional
  public SalonDTO createSalon(SalonDTO salonDTO) {
    User loggedInUser = userService.getLoggedInUser();
    salonDTO.setOwnerId(loggedInUser.getId());

    Salon salon = salonMapper.toEntity(salonDTO);
    salon = salonRepository.save(salon);

    fileService.saveAllImages(salonDTO.getImages(), salon);

    List<SalonWorkday> workdays = new LinkedList<>();
    for (DayOfWeek dayOfWeek : salonDTO.getWorkdays()) {
      workdays.add(new SalonWorkday(salon, dayOfWeek));
    }
    salonWorkdayRepository.saveAll(workdays);
    return salonMapper.toDto(salon);
  }

  public void addEmployeeToSalon(EmployeeDTO employeeDTO) {
    Salon salon = getSalonById(employeeDTO.getSalonId());
    employeeRepository.save(
        new Employee(employeeDTO.getFirstName(), employeeDTO.getLastName(), salon));
  }

  public void addServiceToSalon(ServiceDTO serviceDTO) {
    Salon salon = getSalonById(serviceDTO.getSalonId());
    barberServicesService.addServiceToSalon(salon, serviceDTO);
  }

  public String getCitiesForDropdown(String query) {
    String json = "";
    List<CityItem> cities;
    if (query == null || query.isEmpty()) {
      cities = Arrays.stream(City.values()).map(this::mapToCityItem).collect(Collectors.toList());
    } else {
      String lowerCaseQuery = query.toLowerCase();
      cities =
          Arrays.stream(City.values())
              .filter(city -> city.getDisplayValue().toLowerCase().contains(lowerCaseQuery))
              .map(this::mapToCityItem)
              .collect(Collectors.toList());
    }
    try {
      json = new ObjectMapper().writeValueAsString(cities);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return json;
  }

  private CityItem mapToCityItem(City city) {
    return CityItem.builder().id(city).text(city.getDisplayValue()).slug(city.name()).build();
  }

  public Salon getSalonById(Long id) {
    return salonRepository.findById(id).orElseThrow();
  }

  public Page<SalonDTO> getFirst20Salons() {
    return new PageImpl<>(
        salonRepository.findFirst20ByOrderByNameAsc().stream()
            .map(salonMapper::toDto)
            .collect(Collectors.toList()));
  }

  public SalonDTO getSalonDTO(Long salonId) {
    return salonMapper.toDto(getSalonById(salonId));
  }
}
