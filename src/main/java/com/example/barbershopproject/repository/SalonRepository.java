package com.example.barbershopproject.repository;

import com.example.barbershopproject.model.QSalon;
import com.example.barbershopproject.model.Salon;
import com.example.barbershopproject.model.enumeration.City;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.util.List;

public interface SalonRepository
    extends JpaRepository<Salon, Long>,
        QuerydslPredicateExecutor<Salon>,
        QuerydslBinderCustomizer<QSalon> {

  List<Salon> findFirst20ByOrderByNameAsc();

  List<Salon> findAllByCity(City city);

  @Override
  default void customize(QuerydslBindings bindings, QSalon root) {
    bindings
        .bind(String.class)
        .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
  }
}
