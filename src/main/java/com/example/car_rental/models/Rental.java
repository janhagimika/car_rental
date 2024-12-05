package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Car is required")
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @NotNull(message = "Rental date is required")
    private LocalDateTime rentalDate;

    @Future(message = "Planned return date must be in the future")
    private LocalDateTime plannedReturnDate;

    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private ReturnCondition conditionOnReturn;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @JsonIgnore
    @Version
    private Integer version;

}
