package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findAllBySalon_IdAndSalon_StartTimeLessThanEqualAndSalon_EndTimeGreaterThan(
      Long salonId, LocalTime time, LocalTime t);

  List<Employee> findBySalon_Id(Long salonId);
}
