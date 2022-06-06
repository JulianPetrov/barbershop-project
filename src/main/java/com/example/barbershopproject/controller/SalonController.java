package com.example.barbershopproject.controller;

import com.example.barbershopproject.controller.dto.SalonCreateDTO;
import com.example.barbershopproject.model.SalonServiceEntity;
import com.example.barbershopproject.service.SalonServiceEntityService;
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

    private final SalonServiceEntityService salonServiceEntityService;

    @ModelAttribute("salonDTO")
    public SalonCreateDTO salonDTO() {
        return new SalonCreateDTO();
    }

    @GetMapping("/salon/create")
    public String showSalonCreateForm(SalonCreateDTO salonCreateDTO, Model model) {
        model.addAttribute("salonDTO", salonCreateDTO);
        return "salon/create";
    }

    @PostMapping("/salon/create")
    public ModelAndView createCarListing(
            @Valid @ModelAttribute("salonDTO") SalonCreateDTO salonDTO,
            BindingResult bindingResult,
            Model model)
            throws Exception {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(showSalonCreateForm(salonDTO, model));
        }
        SalonCreateDTO result = salonServiceEntityService.createSalon(salonDTO);
        return new ModelAndView("index");
    }

    @GetMapping("/salon/cities")
    public @ResponseBody String getCities(@RequestParam(value = "q", required = false) String query) {
        return salonServiceEntityService.getCitiesForDropdown(query);
    }
}
