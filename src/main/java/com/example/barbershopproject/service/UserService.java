package com.example.barbershopproject.service;

import com.example.barbershopproject.controller.dto.UserRegistrationDTO;
import com.example.barbershopproject.model.Authority;
import com.example.barbershopproject.model.User;
import com.example.barbershopproject.repository.AuthorityRepository;
import com.example.barbershopproject.repository.UserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthorityRepository authorityRepository;

  public User save(UserRegistrationDTO registrationDto) {
    User user =
        User.builder()
            .firstName(registrationDto.getFirstName())
            .lastName(registrationDto.getLastName())
            .username(registrationDto.getUsername())
            .email(registrationDto.getEmail())
            .password(passwordEncoder.encode(registrationDto.getPassword()))
            .authorities(List.of(authorityRepository.getById("ROLE_USER")))
            .build();
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid username or password.");
    }
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), mapAuthorities(user.getAuthorities()));
  }

  private Collection<? extends GrantedAuthority> mapAuthorities(
      Collection<Authority> authorityCollection) {
    return authorityCollection.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  public boolean userIsLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
  }

  public User findById(Long id) throws NotFoundException {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User getLoggedInUser() {
    if(!userIsLoggedIn()) return null;
    org.springframework.security.core.userdetails.User loggedInUser =
        (org.springframework.security.core.userdetails.User)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsername(loggedInUser.getUsername());
  }
}
