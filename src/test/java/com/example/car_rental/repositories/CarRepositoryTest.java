package com.example.car_rental.repositories;

import com.example.car_rental.models.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setBrand("Honda");
        car.setModel("Civic");
        car.setColor("Blue");
        car.setAvailable(true);
        car.setMileage(50000);
        car.setYearOfManufacture(2018);
        car = carRepository.save(car); // Persist the car entity
    }

    @AfterEach
    void cleanup() {
        carRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        Optional<Car> foundCar = carRepository.findById(car.getId());
        assertTrue(foundCar.isPresent(), "Car should be found");
        assertEquals("Honda", foundCar.get().getBrand(), "Brand should match");
        assertEquals("Civic", foundCar.get().getModel(), "Model should match");
        assertTrue(foundCar.get().isAvailable(), "Car should be available");
    }

    @Test
    void testFindAll() {
        List<Car> cars = carRepository.findAll();
        assertFalse(cars.isEmpty(), "Car list should not be empty");
        assertEquals(1, cars.size(), "Car list size should be 1");
        assertEquals("Honda", cars.get(0).getBrand(), "Brand of the car should match");
    }

    @Test
    void testUpdateCar() {
        car.setColor("Red");
        car.setAvailable(false);
        car.setMileage(60000);

        Car updatedCar = carRepository.save(car); // Save the updated car
        Optional<Car> foundCar = carRepository.findById(updatedCar.getId());

        assertTrue(foundCar.isPresent(), "Updated car should be found");
        assertEquals("Red", foundCar.get().getColor(), "Color should be updated");
        assertFalse(foundCar.get().isAvailable(), "Availability should be updated");
        assertEquals(60000, foundCar.get().getMileage(), "Mileage should be updated");
    }

    @Test
    void testDeleteCar() {
        carRepository.delete(car); // Delete the car
        Optional<Car> deletedCar = carRepository.findById(car.getId());
        assertFalse(deletedCar.isPresent(), "Car should be deleted");
    }
}
