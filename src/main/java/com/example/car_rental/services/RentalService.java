package com.example.car_rental.services;

import com.example.car_rental.models.*;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import jakarta.persistence.OptimisticLockException;
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

        if (!rental.getPlannedReturnDate().isAfter(rental.getRentalDate())) {
            throw new IllegalArgumentException("Planned return date must be after the rental date");
        }
        try {
            Car car = carRepository.findById(rental.getCar().getId())
                    .orElseThrow(() -> new NoSuchElementException("Car not found with ID: " + rental.getCar().getId()));

            if (!car.isAvailable()) {
                throw new RuntimeException("Car is not available for rental");
            }

            Customer customer = customerRepository.findById(rental.getCustomer().getId())
                    .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + rental.getCustomer().getId()));

            car.setAvailable(false);
            carRepository.saveAndFlush(car);

            rental.setCar(car);
            rental.setCustomer(customer);
            rental.setStatus(RentalStatus.PENDING);

            return rentalRepository.save(rental);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Failed to update car availability due to concurrent modification", e);
        }
    }


    @Transactional
    public Rental completeRental(Long rentalId, ReturnCondition conditionOnReturn) {
        try {
            Rental rental = rentalRepository.findById(rentalId)
                    .orElseThrow(() -> new NoSuchElementException("Rental not found"));

            if (rental.getCar() == null || rental.getCar().getId() == null) {
                throw new IllegalArgumentException("Car information is missing");
            }
            if (rental.getCustomer() == null || rental.getCustomer().getId() == null) {
                throw new IllegalArgumentException("Customer information is missing");
            }

            rental.setStatus(RentalStatus.COMPLETED);
            rental.setConditionOnReturn(ReturnCondition.valueOf(conditionOnReturn.name()));
            rental.setReturnDate(LocalDateTime.now());

            Car car = rental.getCar();
            if (conditionOnReturn == ReturnCondition.UNUSABLE) {
                car.setAvailable(false);
            } else {
                car.setAvailable(true);
            }

            carRepository.saveAndFlush(car);
            return rentalRepository.saveAndFlush(rental);

        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Conflict detected while completing rental. Please try again.", e);
        }
    }


}
