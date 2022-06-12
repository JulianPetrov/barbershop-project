package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  List<Appointment> findAllByEmployee_IdAndAppointmentStartBeforeAndAppointmentEndAfter(
      Long employeeId, LocalDateTime startDate, LocalDateTime endDate);

  List<Appointment>
      findAllByEmployee_IdAndAppointmentStartLessThanEqualAndAppointmentEndGreaterThanEqual(
          Long employeeId, LocalDateTime startDate, LocalDateTime endDate);

  @Query(
      "SELECT a from Appointment a WHERE day(a.appointmentStart) = ?1 " +
              "and month(a.appointmentStart) = ?2 and year(a.appointmentStart) = ?3 " +
              "and a.employee.id = ?4 " +
              "order by a.appointmentStart asc ")
  List<Appointment> getBookedAppointmentsForEmployeeForDay(
      int day, int month, int year, long employeeId);

  List<Appointment> findAllByCustomer_Id(Long customerId);

  @Query("SELECT a FROM Appointment a WHERE a.salonServiceEntity.salon.id = ?1")
  List<Appointment> findAllBySalonServiceEntity_Salon_Id(Long salonId);
}
