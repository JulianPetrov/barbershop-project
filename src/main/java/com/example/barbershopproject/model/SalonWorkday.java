package com.example.barbershopproject.model;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "salon_workday",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UC_SalonId_Weekday",
          columnNames = {"salon_id", "week_day"})
    })
public class SalonWorkday {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "salon_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private Salon salon;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "week_day", nullable = false)
  private DayOfWeek weekDay;

  public SalonWorkday(Salon salon, LocalTime startTime, LocalTime endTime, DayOfWeek weekDay) {
    this.salon = salon;
    this.startTime = startTime;
    this.endTime = endTime;
    this.weekDay = weekDay;
  }
}
