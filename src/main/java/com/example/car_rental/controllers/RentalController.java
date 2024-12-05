package com.example.car_rental.controllers;

import com.example.car_rental.models.Rental;
import com.example.car_rental.models.ReturnCondition;
import com.example.car_rental.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<Rental> createRental(@Valid @RequestBody Rental rental) {
        Rental savedRental = rentalService.createRental(rental);
        return ResponseEntity.ok(savedRental);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Long id, @RequestBody ReturnCondition conditionOnReturn) {
        Rental updatedRental = rentalService.completeRental(id, conditionOnReturn);
        return ResponseEntity.ok(updatedRental);
    }

}
