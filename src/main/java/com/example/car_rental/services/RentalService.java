package com.example.car_rental.services;

import com.example.car_rental.models.*;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Transactional
    public Rental createRental(Rental rental) {
        Car car = carRepository.findById(rental.getCar().getId())
                .orElseThrow(() -> new NoSuchElementException("Car not found with ID: " + rental.getCar().getId()));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for rental");
        }

        Customer customer = customerRepository.findById(rental.getCustomer().getId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + rental.getCustomer().getId()));

        // Mark the car as unavailable
        car.setAvailable(false);
        carRepository.save(car);

        // Update rental with managed entities
        rental.setCar(car);
        rental.setCustomer(customer);

        rental.setStatus(RentalStatus.PENDING);
        return rentalRepository.save(rental);
    }


    @Transactional
    public Rental completeRental(Long rentalId, ReturnCondition conditionOnReturn) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NoSuchElementException("Rental not found"));

        if (rental.getCar() == null || rental.getCar().getId() == null) {
            throw new IllegalArgumentException("Car information is missing");
        }
        if (rental.getCustomer() == null || rental.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Customer information is missing");
        }

        // Update rental details
        rental.setStatus(RentalStatus.COMPLETED); // Mark rental as completed
        rental.setConditionOnReturn(ReturnCondition.valueOf(conditionOnReturn.name())); // Save the return condition as a string
        rental.setReturnDate(LocalDateTime.now()); // Set actual return date

        // Mark the car as available (or not if unusable)
        Car car = rental.getCar();
        if(conditionOnReturn == ReturnCondition.UNUSABLE) {
            car.setAvailable(false); // If unusable, car is no longer available
        } else {
            car.setAvailable(true); // Otherwise, mark the car as available
        }
        carRepository.save(car); // Save car state
        return rentalRepository.save(rental); // Save rental
    }


}
