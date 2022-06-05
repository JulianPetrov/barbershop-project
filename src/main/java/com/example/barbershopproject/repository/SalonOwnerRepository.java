package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.SalonOwner;
import com.example.barbershopproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonOwnerRepository extends JpaRepository<SalonOwner, SalonOwner.SalonOwnerId> {
}