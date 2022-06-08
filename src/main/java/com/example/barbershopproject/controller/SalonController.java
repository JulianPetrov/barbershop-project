package com.example.barbershopproject.controller;

import com.example.barbershopproject.controller.dto.EmployeeDTO;
import com.example.barbershopproject.controller.dto.SalonDTO;
import com.example.barbershopproject.controller.dto.ServiceDTO;
import com.example.barbershopproject.service.BarberServicesService;
import com.example.barbershopproject.service.SalonEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SalonController {

  private final SalonEntityService salonEntityService;
  private final BarberServicesService barberServicesService;

  @ModelAttribute("salonDTO")
  public SalonDTO salonDTO() {
    return new SalonDTO();
  }

  @ModelAttribute("employeeDTO")
  public EmployeeDTO employeeDTO() {
    return new EmployeeDTO();
  }

  @GetMapping("/salon/{salonId}")
  public String showSalonDetails(@PathVariable("salonId") Long salonId, Model model) {
    model.addAttribute("salonDTO", salonEntityService.getSalonDTO(salonId));
    return "salon/view";
  }

  @GetMapping("/salon/create")
  public String showSalonCreateForm(SalonDTO salonDTO, Model model) {
    model.addAttribute("salonDTO", salonDTO);
    return "salon/create";
  }

  @PostMapping("/salon/create")
  public ModelAndView createCarListing(
      @Valid @ModelAttribute("salonDTO") SalonDTO salonDTO,
      BindingResult bindingResult,
      Model model)
      throws Exception {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(showSalonCreateForm(salonDTO, model));
    }
    SalonDTO result = salonEntityService.createSalon(salonDTO);
    return new ModelAndView(showSalonDetails(result.getId(), model));
  }

  @GetMapping("/salon/add-employee/{salonId}")
  public String showAddEmployeeForm(
      @PathVariable Long salonId, EmployeeDTO employeeDTO, Model model) {
    employeeDTO.setSalonId(salonEntityService.getSalonById(salonId).getId());
    model.addAttribute("employeeDTO", employeeDTO);
    return "salon/add-employee";
  }

  @PostMapping("/salon/add-employee")
  public ModelAndView addEmployeeToSalon(
      @Valid @ModelAttribute("employeeDTO") EmployeeDTO employeeDTO,
      BindingResult bindingResult,
      Model model)
      throws Exception {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(showAddEmployeeForm(employeeDTO.getSalonId(), employeeDTO, model));
    }
    salonEntityService.addEmployeeToSalon(employeeDTO);
    return new ModelAndView("redirect:/");
  }

  @GetMapping("/salon/add-service/{salonId}")
  public String showAddServiceForm(@PathVariable Long salonId, ServiceDTO serviceDTO, Model model) {
    serviceDTO.setSalonId(salonEntityService.getSalonById(salonId).getId());
    model.addAttribute("serviceDTO", serviceDTO);
    model.addAttribute("servicesList", barberServicesService.getAllServices());
    return "salon/add-service";
  }

  @PostMapping("/salon/add-service")
  public ModelAndView addServiceToSalon(
      @Valid @ModelAttribute("serviceDTO") ServiceDTO serviceDTO,
      BindingResult bindingResult,
      Model model)
      throws Exception {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(showAddServiceForm(serviceDTO.getSalonId(), serviceDTO, model));
    }
    salonEntityService.addServiceToSalon(serviceDTO);
    return new ModelAndView("redirect:/");
  }

  @GetMapping("/salon/cities")
  public @ResponseBody String getCities(@RequestParam(value = "q", required = false) String query) {
    return salonEntityService.getCitiesForDropdown(query);
  }
}
