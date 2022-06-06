package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.BService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BServiceRepository extends JpaRepository<BService, Long> {
}