package com.example.barbershopproject.service;

import com.example.barbershopproject.model.SalonWorkday;
import com.example.barbershopproject.repository.SalonWorkdayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonWorkdayService {
  private final SalonWorkdayRepository salonWorkdayRepository;

  public List<DayOfWeek> getWorkdayDaysOfWeekOfSalon(Long salonId) {
    return salonWorkdayRepository.findBySalon_Id(salonId).stream()
        .map(SalonWorkday::getWeekDay)
        .collect(Collectors.toList());
  }
}
