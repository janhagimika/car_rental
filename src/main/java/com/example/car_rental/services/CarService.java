package com.example.car_rental.services;

import com.example.car_rental.models.Car;
import com.example.car_rental.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found!"));
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public Car updateCar(Long id, Car updatedCar) {
        Car car = getCarById(id);
        car.setBrand(updatedCar.getBrand());
        car.setModel(updatedCar.getModel());
        car.setYearOfManufacture(updatedCar.getYearOfManufacture());
        car.setColor(updatedCar.getColor());
        car.setMileage(updatedCar.getMileage());
        car.setAvailable(updatedCar.isAvailable());
        return carRepository.save(car);
    }
}
