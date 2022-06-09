package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.SalonWorkday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface SalonWorkdayRepository
    extends JpaRepository<SalonWorkday, Long> {
    SalonWorkday findBySalon_IdAndWeekDay(Long id, DayOfWeek weekDay);

    List<SalonWorkday> findBySalon_IdOrderById(Long salonId);

    List<SalonWorkday> findAllByWeekDay(DayOfWeek weekDay);
}
