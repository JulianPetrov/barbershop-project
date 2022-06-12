package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.SalonWorkday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface SalonWorkdayRepository
    extends JpaRepository<SalonWorkday, Long> {
    SalonWorkday findBySalon_IdAndWeekDay(Long id, DayOfWeek weekDay);

    List<SalonWorkday> findBySalon_IdOrderById(Long salonId);
    List<SalonWorkday> findAllByWeekDay(DayOfWeek weekDay);

    @Modifying
    @Query("DELETE FROM SalonWorkday sw WHERE sw.salon.id = ?1")
    void deleteAllBySalonId(@Param("salonId") Long salonId);
}
