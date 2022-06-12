package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.AppointmentDTO;
import com.example.barbershopproject.controller.dto.AvailableTimeslotsDTO;
import com.example.barbershopproject.model.*;
import com.example.barbershopproject.repository.AppointmentRepository;
import com.example.barbershopproject.repository.EmployeeRepository;
import com.example.barbershopproject.repository.SalonServiceEntityRepository;
import com.example.barbershopproject.repository.SalonWorkdayRepository;
import com.example.barbershopproject.service.email.EmailDetails;
import com.example.barbershopproject.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTimeComparator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final UserService userService;
  private final EmployeeRepository employeeRepository;
  private final SalonWorkdayRepository salonWorkdayRepository;
  private final SalonServiceEntityRepository salonServiceEntityRepository;
  private final EmailService emailService;

  @Transactional
  public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
    Appointment appointment = convertToEntity(appointmentDTO);

    List<Appointment> appointmentInThisTimeframe =
        appointmentRepository.findAllByEmployee_IdAndAppointmentStartBeforeAndAppointmentEndAfter(
            appointment.getEmployee().getId(),
            appointment.getAppointmentStart(),
            appointment.getAppointmentStart());
    if (!appointmentInThisTimeframe.isEmpty()) {
      return null;
    }
    appointment = appointmentRepository.save(appointment);
    emailService.sendAppointmentConfirmationEmail(appointment);
    return convertToDto(appointment);
  }


  public AppointmentDTO getAppointmentDTO(Long appointmentId) {
    return convertToDto(appointmentRepository.findById(appointmentId).orElseThrow());
  }

  private Appointment convertToEntity(AppointmentDTO appointmentDTO) {
    SalonServiceEntity salonServiceEntity =
        salonServiceEntityRepository.findById(appointmentDTO.getSalonServiceId()).orElseThrow();
    BService service = salonServiceEntity.getService();
    User loggedInUser = userService.getLoggedInUser();

    Employee barber = employeeRepository.findById(appointmentDTO.getEmployeeId()).orElseThrow();
    LocalDateTime startTime =
        LocalDateTime.of(appointmentDTO.getDate(), LocalTime.parse(appointmentDTO.getTime()));
    LocalDateTime endTime = startTime.plusMinutes(service.getDurationMinutes());
    return Appointment.builder()
        .id(appointmentDTO.getId())
        .customer(loggedInUser)
        .employee(barber)
        .salonServiceEntity(salonServiceEntity)
        .appointmentStart(startTime)
        .appointmentEnd(endTime)
        .isFinished(false)
        .build();
  }

  private AppointmentDTO convertToDto(Appointment appointment) {
    Salon salon = appointment.getSalonServiceEntity().getSalon();
    Employee employee = appointment.getEmployee();
    User customer = appointment.getCustomer();
    return AppointmentDTO.builder()
        .id(appointment.getId())
        .salonId(salon.getId())
        .employeeId(employee.getId())
        .salonServiceId(appointment.getSalonServiceEntity().getId())
        .date(appointment.getAppointmentStart().toLocalDate())
        .time(
            appointment
                .getAppointmentStart()
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm")))
        .endDateTime(appointment.getAppointmentEnd())
        .isFinished(appointment.getIsFinished())
        .customerUsername(customer.getUsername())
        .canBeCancelled(
            userService.userIsLoggedIn()
                && (userService.getLoggedInUser().getAuthorities().stream()
                        .anyMatch(a -> a.getName().equals("ROLE_ADMIN"))
                    || userService.getLoggedInUser().getUsername().equals(customer.getUsername()))
                && appointment.getAppointmentStart().isAfter(LocalDateTime.now().plusMinutes(60)))
        .salonName(salon.getName())
        .customerFullName(String.format("%s %s", customer.getFirstName(), customer.getLastName()))
        .employeeFullName(String.format("%s %s", employee.getFirstName(), employee.getLastName()))
        .startDateTime(appointment.getAppointmentStart())
        .build();
  }

  public List<Salon> getAllAvailableSalonsForDateTime(LocalDateTime dateTime) {
    DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
    return salonWorkdayRepository.findAllByWeekDay(dayOfWeek).stream()
        .map(SalonWorkday::getSalon)
        .filter(salon -> salonIsAvailableForAppointment(salon, dateTime))
        .collect(Collectors.toList());
  }

  public boolean salonIsAvailableForAppointment(Salon salon, LocalDateTime dateTime) {
    LocalTime time = dateTime.toLocalTime();
    DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
    SalonWorkday salonWorkday =
        salonWorkdayRepository.findBySalon_IdAndWeekDay(salon.getId(), dayOfWeek);
    if (salonWorkday == null) return false;

    List<Employee> availableEmployees =
        employeeRepository
            .findAllBySalon_IdAndSalon_StartTimeLessThanEqualAndSalon_EndTimeGreaterThan(
                salon.getId(), time, time)
            .stream()
            .filter(
                employee ->
                    getAppointmentsByEmployeeIdAndDateTime(employee.getId(), dateTime, dateTime)
                        .isEmpty())
            .collect(Collectors.toList());

    return !availableEmployees.isEmpty();
  }

  public List<Appointment> getAppointmentsByEmployeeIdAndDateTime(
      Long employeeId, LocalDateTime startTime, LocalDateTime endTime) {
    return appointmentRepository
        .findAllByEmployee_IdAndAppointmentStartLessThanEqualAndAppointmentEndGreaterThanEqual(
            employeeId, startTime, endTime);
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

  public List<String> getAllTimesForDate(Long salonId, LocalDate date) {
    List<String> timesForDay = new LinkedList<>();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    SalonWorkday salonWorkday = salonWorkdayRepository.findBySalon_IdAndWeekDay(salonId, dayOfWeek);
    if (salonWorkday == null) return timesForDay;

    Salon salon = salonWorkday.getSalon();
    for (LocalTime time = salon.getStartTime();
        time.isBefore(salon.getEndTime());
        time = time.plusMinutes(15)) {
      timesForDay.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
    return timesForDay;
  }

  public List<String> getAllAvailableTimesForEmployeeOnDate(
      Long employeeId, Long salonServiceId, LocalDate date) {
    Employee employee = employeeRepository.findById(employeeId).orElseThrow();
    Salon salon = employee.getSalon();
    SalonServiceEntity salonService =
        salonServiceEntityRepository.findById(salonServiceId).orElseThrow();
    Integer serviceDurationInMinutes = salonService.getService().getDurationMinutes();

    Map<String, List<Appointment>> appointmentsMap =
        appointmentRepository
            .getBookedAppointmentsForEmployeeForDay(
                date.getDayOfMonth(), date.getMonthValue(), date.getYear(), employeeId)
            .stream()
            .collect(
                groupingBy(
                    a -> a.getAppointmentStart().format(DateTimeFormatter.ofPattern("HH:mm"))));

    LocalDateTime tempEndTime = null;
    List<String> availableTimes = new LinkedList<>();
    for (String time : getAllTimesForDate(salon.getId(), date)) {
      if (appointmentsMap.get(time) != null) {
        Appointment appointment = appointmentsMap.get(time).get(0);
        tempEndTime = appointment.getAppointmentEnd().minusMinutes(15);
        continue;
      }

      if (tempEndTime != null) {
        String formattedTempEndTime =
            tempEndTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        if (formattedTempEndTime.equals(time)) {
          tempEndTime = null;
        }
        continue;
      }

      boolean shouldSkip = false;
      for (int i = 0; i < serviceDurationInMinutes / 15; i++) {
        String timeWithAddedCurrentServiceDuration =
            LocalTime.parse(time).plusMinutes(i * 15).format(DateTimeFormatter.ofPattern("HH:mm"));
        if (appointmentsMap.get(timeWithAddedCurrentServiceDuration) != null) {
          shouldSkip = true;
          break;
        }
      }

      if (!shouldSkip) availableTimes.add(time);
    }

    return availableTimes;

    /*getAllTimesForDate(salon.getId(), date).stream()
    .filter(
        time -> {
          LocalDateTime startTime = LocalDateTime.of(date, LocalTime.parse(time));
          LocalDateTime endTime = startTime.plusMinutes(serviceDurationInMinutes);
          List<Appointment> appointmentsList = getAppointmentsByEmployeeIdAndDateTime(employeeId, startTime, endTime);
          return appointmentsList.isEmpty();
        })
    .collect(Collectors.toList())*/
  }

  public void cancelAppointmentById(Long appointmentId) {
    AppointmentDTO appointmentDTO = getAppointmentDTO(appointmentId);
    Appointment appointment = appointmentRepository.getById(appointmentId);
    if (appointmentDTO.isCanBeCancelled()) {
      appointmentRepository.deleteById(appointmentId);
      emailService.sendAppointmentCancellationEmail(appointment);
    }
  }

  public List<AppointmentDTO> getAllAppointmentsOfUser() {
    return appointmentRepository
        .findAllByCustomer_Id(userService.getLoggedInUser().getId())
        .stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public List<AppointmentDTO> getAllAppointmentsOfSalon(Long salonId) {
    return appointmentRepository.findAllBySalonServiceEntity_Salon_Id(salonId).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }
}
