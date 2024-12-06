package com.example.car_rental.services;

import com.example.car_rental.models.Car;
import com.example.car_rental.repositories.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car saveCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        return carRepository.save(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new NoSuchElementException("Car not found");
        }
        carRepository.deleteById(id);
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByIsAvailableTrue();
    }

    @Transactional
    public Car updateCar(Long id, Car updatedCar) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Car not found!"));

        if (!existingCar.isAvailable()) {
            throw new IllegalStateException("Car is not available for updates");
        }

        existingCar.setBrand(updatedCar.getBrand());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYearOfManufacture(updatedCar.getYearOfManufacture());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setMileage(updatedCar.getMileage());
        existingCar.setAvailable(updatedCar.isAvailable());

        return carRepository.save(existingCar);
    }

}
