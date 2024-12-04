package com.example.car_rental.services;

import com.example.car_rental.models.*;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for rental");
        }

        Customer customer = customerRepository.findById(rental.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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
    public Rental completeRental(Long rentalId, Rental rentalDetails) {

        if (rentalDetails.getCar() == null || rentalDetails.getCar().getId() == null) {
            throw new IllegalArgumentException("Car information is missing");
        }
        if (rentalDetails.getCustomer() == null || rentalDetails.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Customer information is missing");
        }

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setStatus(RentalStatus.COMPLETED);
        rental.setConditionOnReturn(rentalDetails.getConditionOnReturn());
        rental.setReturnDate(LocalDateTime.now());

        Car car = rental.getCar();

        // Update car availability based on return condition
        if (rentalDetails.getConditionOnReturn() == ReturnCondition.DAMAGED ||
                rentalDetails.getConditionOnReturn() == ReturnCondition.UNUSABLE) {
            car.setAvailable(false);
        } else {
            car.setAvailable(true);
        }

        carRepository.save(car);
        rentalRepository.save(rental);

        return rental;
    }


}
