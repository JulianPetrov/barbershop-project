package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.AppointmentCreateDTO;
import com.example.barbershopproject.controller.dto.AvailableTimeslotsDTO;
import com.example.barbershopproject.model.*;
import com.example.barbershopproject.repository.AppointmentRepository;
import com.example.barbershopproject.repository.EmployeeRepository;
import com.example.barbershopproject.repository.SalonWorkdayRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTimeComparator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final UserService userService;
  private final EmployeeRepository employeeRepository;
  private final SalonWorkdayRepository salonWorkdayRepository;

  @Transactional
  public void createAppointment(AppointmentCreateDTO appointmentCreateDTO) {
    SalonServiceEntity salonServiceEntity = appointmentCreateDTO.getSalonServiceEntity();
    BService service = salonServiceEntity.getService();
    User loggedInUser = userService.getLoggedInUser();

    Employee barber = appointmentCreateDTO.getBarber();
    LocalDateTime startTime = appointmentCreateDTO.getStartTime();
    LocalDateTime endTime = startTime.plusMinutes(service.getDurationMinutes());
    List<Appointment> appointmentInThisTimeframe =
        appointmentRepository.findAllByEmployee_IdAndAppointmentStartAfterAndAppointmentEndBefore(
            barber.getId(), startTime, endTime);
    if (!appointmentInThisTimeframe.isEmpty()) {
      return;
    }

    Appointment appointment =
        Appointment.builder()
            .customer(loggedInUser)
            .employee(barber)
            .salonServiceEntity(salonServiceEntity)
            .appointmentStart(startTime)
            .appointmentEnd(endTime)
            .isFinished(false)
            .build();
  }

  public AvailableTimeslotsDTO getAllAvailableTimeslotsOfEmployeeForDate(
      Long employeeId, LocalDateTime date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    Employee employee = employeeRepository.findById(employeeId).orElseThrow();
    Salon salon = employee.getSalon();
    SalonWorkday salonWorkday =
        salonWorkdayRepository.findBySalon_IdAndWeekDay(salon.getId(), dayOfWeek);
    if (salonWorkday == null) return null;

    LocalTime startTime = salon.getStartTime();
    LocalTime endTime = salon.getEndTime();

    List<LocalDateTime> availableAppointmentsForEmployeeForDay =
        appointmentRepository
            .getBookedAppointmentsForEmployeeForDay(
                date.getDayOfMonth(), date.getMonthValue(), date.getYear(), employeeId)
            .stream()
            .map(Appointment::getAppointmentStart)
            .collect(Collectors.toList());

    List<LocalDateTime> availableTimeslots = new LinkedList<>();
    for (LocalTime timeslot = startTime;
        timeslot.isBefore(endTime);
        timeslot = timeslot.plusMinutes(30)) {
      boolean skip = false;
      for (LocalDateTime appointmentStartTime : availableAppointmentsForEmployeeForDay) {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        if (comparator.compare(appointmentStartTime, timeslot) == 0) {
          skip = true;
          break;
        }
      }

      if (!skip) {
        availableTimeslots.add(LocalDateTime.of(date.toLocalDate(), timeslot));
      }
    }
    return new AvailableTimeslotsDTO(availableTimeslots);
  }
}
