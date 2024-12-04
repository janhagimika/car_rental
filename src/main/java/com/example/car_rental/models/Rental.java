package com.example.car_rental.models;

import com.example.car_rental.settings.LocalDateToLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    private LocalDateTime rentalDate;

    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private ReturnCondition conditionOnReturn;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @JsonIgnore
    @Version
    private Integer version; // Optimistic locking version field


}
