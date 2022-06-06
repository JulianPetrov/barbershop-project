package com.example.barbershopproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "username", length = 50, unique = true, nullable = false)
  private String username;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash", length = 60, nullable = false)
  private String password;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  @NotNull
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
  @NotNull
  private String lastName;

  @Email
  @Size(min = 5, max = 254)
  @Column(name = "email", length = 254, unique = true)
  private String email;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_user_authority",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
  @BatchSize(size = 20)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @ToString.Exclude
  private Collection<Authority> authorities;
}
