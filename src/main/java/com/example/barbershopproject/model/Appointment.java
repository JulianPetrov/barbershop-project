package com.example.barbershopproject.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appointment")
public class Appointment{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ToString.Exclude
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ToString.Exclude
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salon_service_id", referencedColumnName = "id")
    @ToString.Exclude
    private SalonServiceEntity salonServiceEntity;

    @Column(name = "appointment_start", nullable = false)
    private LocalDateTime appointmentStart;

    @Column(name = "appointment_end", nullable = false)
    private LocalDateTime appointmentEnd;
}
