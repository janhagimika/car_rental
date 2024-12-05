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
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByIsAvailableTrue();
    }
    @Transactional
    public Car updateCar(Long id, Car updatedCar) {
        Car car = carRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Car not found!"));
        car.setBrand(updatedCar.getBrand());
        car.setModel(updatedCar.getModel());
        car.setYearOfManufacture(updatedCar.getYearOfManufacture());
        car.setColor(updatedCar.getColor());
        car.setMileage(updatedCar.getMileage());
        car.setAvailable(updatedCar.isAvailable());
        return carRepository.save(car);
    }
}
