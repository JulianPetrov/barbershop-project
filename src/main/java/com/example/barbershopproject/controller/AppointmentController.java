package com.example.barbershopproject.controller;

import com.example.barbershopproject.controller.dto.AppointmentDTO;
import com.example.barbershopproject.service.AppointmentService;
import com.example.barbershopproject.service.BarberServicesService;
import com.example.barbershopproject.service.SalonEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;
  private final SalonEntityService salonEntityService;
  private final BarberServicesService barberServicesService;

  @ModelAttribute("appointmentDTO")
  public AppointmentDTO salonDTO() {
    return new AppointmentDTO();
  }

  @GetMapping("/salon/{salonId}/appointment/create")
  public String showCreateAppointmentForm(
      @PathVariable(name = "salonId") Long salonId, AppointmentDTO appointmentDTO, Model model) {
    appointmentDTO.setSalonId(salonId);
    model.addAttribute("appointmentDTO", appointmentDTO);
    model.addAttribute("employees", salonEntityService.getAllEmployeesOfSalon(salonId));
    model.addAttribute("services", barberServicesService.getAllServicesOfSalon(salonId));
    return "appointment/create";
  }

  @PostMapping("/appointment/create")
  public ModelAndView createAppointment(
      @Valid @ModelAttribute("appointmentDTO") AppointmentDTO appointmentDTO,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(
          showCreateAppointmentForm(appointmentDTO.getSalonId(), appointmentDTO, model));
    }
    AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);
    return new ModelAndView(getAllAppointmentsOfUser(model));
  }

  @GetMapping("/appointment/{appointmentId}")
  public String showAppointmentDetails(
      @PathVariable("appointmentId") Long appointmentId, Model model) {
    model.addAttribute("appointmentDTO", appointmentService.getAppointmentDTO(appointmentId));
    return "appointment/view";
  }

  @GetMapping("/appointment/my-appointments")
  public String getAllAppointmentsOfUser(Model model) {
    List<AppointmentDTO> appointmentsList = appointmentService.getAllAppointmentsOfUser();
    model.addAttribute("appointmentsList", appointmentsList);
    return "appointment/index";
  }


  @GetMapping("/appointment/{appointmentId}/cancel")
  public ModelAndView cancelAppointment(
          @PathVariable("appointmentId") Long appointmentId, Model model) {
    appointmentService.cancelAppointmentById(appointmentId);
    return new ModelAndView(getAllAppointmentsOfUser(model));
  }

  @GetMapping("/salon/available-times")
  public @ResponseBody String getAvailableTimes(
      @RequestParam(required = false) Long employeeId,
      @RequestParam(required = false) Long serviceId,
      @RequestParam(required = false) LocalDate date) {
    String responseJson = null;
    List<String> availableTimes = new LinkedList<>();
    if (employeeId != null && date != null) {
      availableTimes.addAll(appointmentService.getAllAvailableTimesForEmployeeOnDate(employeeId, serviceId, date));
    }
    try {
      responseJson = new ObjectMapper().writeValueAsString(availableTimes);
    } catch (JsonProcessingException e) {

      e.printStackTrace();
    }
    return responseJson;
  }
}
