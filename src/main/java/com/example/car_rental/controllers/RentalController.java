package com.example.car_rental.controllers;

import com.example.car_rental.models.Rental;
import com.example.car_rental.models.ReturnCondition;
import com.example.car_rental.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "API for managing rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Operation(summary = "Create a new rental", description = "Record a new car rental by providing car ID, customer ID, rental date, and planned return date. Planned Date must be after rental date and in the future.")
    @ApiResponse(responseCode = "201", description = "Rental created successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid rental details provided related to car or customer.")
    @ApiResponse(responseCode = "404", description = "Invalid rental details provided related dates.")
    @PostMapping
    public ResponseEntity<Rental> createRental( @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request to create a rental. Only `car.id`, `customer.id`, `rentalDate`, and `plannedReturnDate` are required.",
            required = true,
            content = @Content(
                    schema = @Schema(example =
                            "{\n" +
                                    "  \"car\": {\"id\": 1},\n" +
                                    "  \"customer\": {\"id\": 2},\n" +
                                    "  \"rentalDate\": \"2024-12-06T03:34:09.752Z\",\n" +
                                    "  \"plannedReturnDate\": \"2024-12-10T03:34:09.752Z\"\n" +
                                    "}"
                    )
            )
    ) @Valid @RequestBody Rental rental) {
        Rental savedRental = rentalService.createRental(rental);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRental.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedRental);
    }
    @Operation(
            summary = "Complete a rental",
            description = "Record the return of a rented car by providing the condition of the car upon return. The return date is set automatically to the time of the operation."
    )
    @ApiResponse(responseCode = "200", description = "Rental completed successfully.")
    @ApiResponse(responseCode = "404", description = "Rental not found.")
    @PutMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Condition of the car upon return. Options: 'EXCELLENT', 'GOOD', 'DAMAGED', 'UNUSABLE'.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReturnCondition.class, example = "GOOD")
                    )
            )
            @RequestBody ReturnCondition conditionOnReturn) {
        Rental updatedRental = rentalService.completeRental(id, conditionOnReturn);
        return ResponseEntity.ok(updatedRental);
    }

}
