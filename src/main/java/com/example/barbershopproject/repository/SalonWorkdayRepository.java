package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Salon;
import com.example.barbershopproject.model.SalonWorkday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonWorkdayRepository
    extends JpaRepository<SalonWorkday, SalonWorkday.SalonWorkdayId> {}
