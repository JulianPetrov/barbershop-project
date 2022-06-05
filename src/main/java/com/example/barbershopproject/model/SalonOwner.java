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
@Table(name = "salon_owner")
@IdClass(value = SalonOwner.SalonOwnerId.class)
public class SalonOwner {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private User user;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "salon_id", referencedColumnName = "id", nullable = false)
  @ToString.Exclude
  private Salon salon;

  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SalonOwnerId implements Serializable {
    private Long user;
    private Long salon;
  }
}
