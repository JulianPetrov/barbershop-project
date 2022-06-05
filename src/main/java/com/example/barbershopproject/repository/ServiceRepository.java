package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}