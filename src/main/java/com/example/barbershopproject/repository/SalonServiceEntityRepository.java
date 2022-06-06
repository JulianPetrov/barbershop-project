package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.SalonServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalonServiceEntityRepository extends JpaRepository<SalonServiceEntity, Long> {

    List<SalonServiceEntity> findAllBySalon_Id(Long salonId);
}