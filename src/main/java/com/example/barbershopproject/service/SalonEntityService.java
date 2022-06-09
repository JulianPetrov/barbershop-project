package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.EmployeeDTO;
import com.example.barbershopproject.controller.dto.SalonDTO;
import com.example.barbershopproject.controller.dto.SalonSearchDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

  private final AppointmentService appointmentService;

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

  public List<SalonDTO> getFirst20Salons() {
    return salonRepository.findFirst20ByOrderByNameAsc().stream()
        .map(salonMapper::toDto)
        .collect(Collectors.toList());
  }

  public SalonDTO getSalonDTO(Long salonId) {
    return salonMapper.toDto(getSalonById(salonId));
  }

  public List<SalonDTO> searchSalons(SalonSearchDTO salonSearchDTO) {
    List<Salon> salons = new LinkedList<>();
    boolean isQueried = false;

    if (salonSearchDTO.getCity() != null) {
      salons.addAll(salonRepository.findAllByCity(salonSearchDTO.getCity()));
      isQueried = true;
    }

    if (salonSearchDTO.getServiceId() != null && salonSearchDTO.getServiceId() != -1) {
      if (isQueried) {
        salons.retainAll(barberServicesService.getSalonsByServiceId(salonSearchDTO.getServiceId()));
      } else {
        salons.addAll(barberServicesService.getSalonsByServiceId(salonSearchDTO.getServiceId()));
        isQueried = true;
      }
    }

    if (salonSearchDTO.getDate() != null && salonSearchDTO.getTime() != null) {
      LocalTime time = LocalTime.parse(salonSearchDTO.getTime());
      LocalDateTime dateTime = LocalDateTime.of(salonSearchDTO.getDate(), time);
      if (isQueried) {
        salons.retainAll(appointmentService.getAllAvailableSalonsForDateTime(dateTime));
      } else {
        salons.addAll(appointmentService.getAllAvailableSalonsForDateTime(dateTime));
      }
    }

    return salonMapper.toDto(salons);
  }

  public List<EmployeeDTO> getAllEmployeesOfSalon(Long salonId) {
    return employeeRepository.findBySalon_Id(salonId).stream()
        .map(this::convertEmployeeToDto)
        .collect(Collectors.toList());
  }

  private EmployeeDTO convertEmployeeToDto(Employee employee) {
    return EmployeeDTO.builder()
        .id(employee.getId())
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .salonId(employee.getSalon().getId())
        .build();
  }
}
