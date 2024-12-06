package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Schema(description = "Represents a rental transaction.")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Car is required")
    @Schema(description = "The car being rented.", implementation = Car.class)
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @NotNull(message = "Customer is required")
    @Schema(description = "The customer renting the car.", implementation = Customer.class)
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @NotNull(message = "Rental date is required")
    @Schema(description = "The date and time the rental starts.", example = "2024-12-06T10:00:00")
    private LocalDateTime rentalDate;

    @Future(message = "Planned return date must be in the future")
    @Schema(description = "The planned return date and time.", example = "2024-12-10T10:00:00")
    private LocalDateTime plannedReturnDate;

    @Schema(description = "The actual return date and time, if completed.", example = "2024-12-09T15:00:00")
    private LocalDateTime returnDate;

    @Schema(description = "Condition of the car upon return.", example = "GOOD")
    @Enumerated(EnumType.STRING)
    private ReturnCondition conditionOnReturn;

    @Schema(description = "Status of the rental.", example = "PENDING")
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @JsonIgnore
    @Schema(description = "Version for optimistic locking. Ignored in API responses.")
    @Version
    private Integer version;

}
