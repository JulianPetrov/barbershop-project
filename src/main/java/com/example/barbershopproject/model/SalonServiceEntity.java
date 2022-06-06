package com.example.barbershopproject.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salon_service")
public class SalonServiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "salon_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private Salon salon;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private BService service;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  public SalonServiceEntity(Salon salon, BService service, BigDecimal price) {
    this.salon = salon;
    this.service = service;
    this.price = price;
  }
}
