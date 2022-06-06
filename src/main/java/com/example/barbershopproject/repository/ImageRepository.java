package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllBySalon_Id(Long salonId);
}
