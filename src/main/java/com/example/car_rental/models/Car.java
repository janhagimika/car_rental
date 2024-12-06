package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year of manufacture is required")
    private int yearOfManufacture;

    @NotBlank(message = "Color is required")
    private String color;

    @Min(value = 0, message = "Mileage must be a positive number")
    private int mileage;

    private boolean isAvailable = true;

    @JsonIgnore
    @OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rental> rentals;

    @JsonIgnore
    @Version
    private Integer version;

}
