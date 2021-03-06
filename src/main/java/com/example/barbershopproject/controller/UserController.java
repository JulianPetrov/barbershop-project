package com.example.barbershopproject.controller;

import com.example.barbershopproject.controller.dto.UserRegistrationDTO;
import com.example.barbershopproject.service.SalonEntityService;
import com.example.barbershopproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final SalonEntityService salonEntityService;

  @GetMapping("/login")
  public String login(Model model) {
    if (userService.userIsLoggedIn()) return home(model);
    return "login";
  }

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("salonsList", salonEntityService.getFirst20Salons());
    return "index";
  }

  @ModelAttribute("user")
  public UserRegistrationDTO userRegistrationDto() {
    return new UserRegistrationDTO();
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    if (userService.userIsLoggedIn()) return home(model);
    return "register";
  }

  @PostMapping("/register")
  public String registerUserAccount(@ModelAttribute("user") UserRegistrationDTO registrationDto) {
    userService.save(registrationDto);
    return "redirect:/register?success";
  }
}
