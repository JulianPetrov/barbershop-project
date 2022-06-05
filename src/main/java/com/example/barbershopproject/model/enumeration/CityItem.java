package com.example.barbershopproject.model.enumeration;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityItem {
    private City id;
    private String text;
    private String slug;
}
