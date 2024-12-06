package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Schema(description = "Represents a car in the rental system.")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the car. This field is auto-generated and should not be included in requests.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Brand is required")
    @Schema(description = "The brand of the car.", example = "Toyota")
    private String brand;

    @NotBlank(message = "Model is required")
    @Schema(description = "The model of the car.", example = "Camry")
    private String model;

    @NotNull(message = "Year of manufacture is required")
    @Schema(description = "The year the car was manufactured.", example = "2021")
    private int yearOfManufacture;

    @NotBlank(message = "Color is required")
    @Schema(description = "The color of the car.", example = "Blue")
    private String color;

    @Min(value = 0, message = "Mileage must be a positive number")
    @Schema(description = "The car's mileage in kilometers.", example = "50000")
    private int mileage;

    @Schema(description = "Indicates whether the car is available for rental.", example = "true")
    private boolean isAvailable;

    @JsonIgnore
    @Schema(description = "List of rentals associated with the car. Ignored in API responses.")
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals;

    @JsonIgnore
    @Schema(description = "Version for optimistic locking. Ignored in API responses.")
    @Version
    private Integer version;

}
