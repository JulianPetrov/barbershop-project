package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {
}