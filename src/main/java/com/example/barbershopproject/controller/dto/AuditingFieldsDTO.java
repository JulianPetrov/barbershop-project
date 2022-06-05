package com.example.barbershopproject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditingFieldsDTO implements Serializable {
    
    @ReadOnlyProperty
    private Instant createdDate;

    @ReadOnlyProperty
    private Instant lastModifiedDate;
}
