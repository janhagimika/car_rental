package com.example.car_rental.controllers;

import com.example.car_rental.models.Car;
import com.example.car_rental.services.CarService;
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
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Cars", description = "API for managing cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Operation(summary = "Get all cars", description = "Retrieve a list of all cars in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of cars.")
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @Operation(summary = "Get available cars", description = "Retrieve a list of cars currently available for rental.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of available cars.")
    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> availableCars = carService.getAvailableCars();
        return ResponseEntity.ok(availableCars);
    }

    @Operation(summary = "Add a new car", description = "Add a new car to the inventory.")
    @ApiResponse(responseCode = "201", description = "Car added successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid car details provided.")
    @PostMapping
    public ResponseEntity<Car> saveCar(@Valid @RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCar.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCar); // Returns 201 Created
    }

    @Operation(summary = "Update car details", description = "Update details of an existing car.")
    @ApiResponse(responseCode = "200", description = "Car updated successfully.")
    @ApiResponse(responseCode = "404", description = "Car not found.")
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @Valid @RequestBody Car updatedCar) {
        return carService.updateCar(id, updatedCar);
    }

    @Operation(summary = "Delete a car", description = "Delete a car from the inventory by its ID.")
    @ApiResponse(responseCode = "200", description = "Car deleted successfully.",
            content = @Content(
            schema = @Schema(type = "string", example = "Car deleted successfully!")
            )
    )
    @ApiResponse(responseCode = "404", description = "Car not found.",
            content = @Content(
                    schema = @Schema(type = "string", example = "Car not found!")
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully!");
    }

}
