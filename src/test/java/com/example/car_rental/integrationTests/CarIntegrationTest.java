package com.example.car_rental.integrationTests;

import com.example.car_rental.models.Car;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.Rental;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import com.example.car_rental.services.CarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CarIntegrationTest {

    @Autowired
    private CarService carService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testDeleteCarWithRentals() {
        // Arrange
        Car car = new Car();
        car.setBrand("Honda");
        car.setModel("Civic");
        car.setColor("Blue");
        car.setAvailable(true);
        car.setMileage(50000);
        car.setYearOfManufacture(2018);
        car = carRepository.save(car);

        Customer customer = new Customer();
        customer.setFirstname("John");
        customer.setSurname("Doe");
        customer.setAddress("123 Main St");
        customer.setPhoneNumber("123456789");
        customer.setEmailAddress("john.doe@example.com");
        customer = customerRepository.save(customer);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalDate(LocalDateTime.now());
        rental = rentalRepository.save(rental);

        // Act
        carService.deleteCar(car.getId());

        // Assert
        assertFalse(carRepository.findById(car.getId()).isPresent(), "Car should be deleted");
        assertFalse(rentalRepository.findById(rental.getId()).isPresent(), "Rental should be deleted");
    }

    @AfterEach
    void cleanup() {
        customerRepository.deleteAll();
    }

}
