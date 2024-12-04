package com.example.car_rental.controllers;

import com.example.car_rental.models.Rental;
import com.example.car_rental.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public Rental saveRental(@RequestBody Rental rental) {
        return rentalService.saveRental(rental);
    }
}
