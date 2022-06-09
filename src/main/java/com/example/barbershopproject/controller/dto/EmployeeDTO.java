package com.example.barbershopproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

  private Long id;

  @NotNull(message = "First name required.")
  private String firstName;

  @NotNull(message = "Last name required.")
  private String lastName;

  @NotNull(message = "Salon required.")
  private Long salonId;
}
