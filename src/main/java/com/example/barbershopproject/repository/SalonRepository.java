package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonRepository extends JpaRepository<Salon, Long> {
}