package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private int yearOfManufacture;

    private String color;

    private int mileage;

    private boolean isAvailable = true;

    @JsonIgnore
    @Version
    private Integer version; // Optimistic locking version field

}
