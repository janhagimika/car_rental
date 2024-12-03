package com.example.car_rental.controllers;

import com.example.car_rental.models.Rental;
import com.example.car_rental.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @PostMapping
    public Rental saveRental(@RequestBody Rental rental) {
        return rentalService.saveRental(rental);
    }
}
