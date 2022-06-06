package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.EmployeeDTO;
import com.example.barbershopproject.controller.dto.SalonCreateDTO;
import com.example.barbershopproject.controller.dto.ServiceAddDTO;
import com.example.barbershopproject.model.*;
import com.example.barbershopproject.model.enumeration.City;
import com.example.barbershopproject.model.enumeration.CityItem;
import com.example.barbershopproject.model.enumeration.Weekday;
import com.example.barbershopproject.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonServiceEntityService {

  private final SalonRepository salonRepository;
  private final SalonWorkdayRepository salonWorkdayRepository;
  private final SalonOwnerRepository salonOwnerRepository;
  private final SalonServiceEntityRepository salonServiceEntityRepository;
  private final EmployeeRepository employeeRepository;
  private final UserService userService;
  private final FileService fileService;
  private final BServiceRepository serviceRepository;

  @Transactional
  public SalonCreateDTO createSalon(SalonCreateDTO salonCreateDTO) {
    Salon salon =
        Salon.builder()
            .name(salonCreateDTO.getName())
            .address(salonCreateDTO.getAddress())
            .city(salonCreateDTO.getCity())
            .build();
    salon = salonRepository.save(salon);
    salonCreateDTO.setId(salon.getId());

    fileService.saveAllImages(salonCreateDTO.getImages(), salon);

    User loggedInUser = userService.getLoggedInUser();
    salonOwnerRepository.save(new SalonOwner(loggedInUser, salon));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime startTime = LocalTime.parse(salonCreateDTO.getStartTime(), formatter);
    LocalTime endTime = LocalTime.parse(salonCreateDTO.getEndTime());
    List<SalonWorkday> workdays = new LinkedList<>();
    for (DayOfWeek dayOfWeek : salonCreateDTO.getWorkdays()) {
      workdays.add(new SalonWorkday(salon, startTime, endTime, dayOfWeek));
    }
    salonWorkdayRepository.saveAll(workdays);
    return salonCreateDTO;
  }

  public void addEmployeeToSalon(EmployeeDTO employeeDTO) {
    employeeRepository.save(
        new Employee(
            employeeDTO.getFirstName(), employeeDTO.getLastName(), employeeDTO.getSalon()));
  }

  public void addServiceToSalon(ServiceAddDTO serviceAddDTO) {
    salonServiceEntityRepository.save(
        new SalonServiceEntity(
            serviceAddDTO.getSalon(), serviceAddDTO.getService(), serviceAddDTO.getPrice()));
  }

  public List<BService> getAllServicesOfSalon(Long salonId) {
    return salonServiceEntityRepository.findAllBySalon_Id(salonId).stream()
        .map(SalonServiceEntity::getService)
        .collect(Collectors.toList());
  }

  public List<BService> getAllServices() {
    return serviceRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
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
}
