package com.example.barbershopproject.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salon_workday")
@IdClass(value = SalonWorkday.SalonWorkdayId.class)
public class SalonWorkday {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "salon_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private Salon salon;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "workday_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private Workday workday;

  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SalonWorkdayId implements Serializable {
    private Long salon;
    private Long workday;
  }
}
