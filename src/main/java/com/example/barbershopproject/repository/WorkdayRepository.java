package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Workday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {
}