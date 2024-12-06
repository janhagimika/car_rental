package com.example.car_rental.integrationTests;

import com.example.car_rental.models.Car;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.Rental;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import com.example.car_rental.services.RentalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RentalIntegrationTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testRentingCar() {
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
        rental.setPlannedReturnDate(LocalDateTime.now().plusDays(5));

        // Act
        Rental savedRental = rentalService.createRental(rental);

        // Assert
        assertNotNull(savedRental);
        assertFalse(carRepository.findById(car.getId()).orElseThrow().isAvailable());
        assertEquals(customer.getId().longValue(), savedRental.getCustomer().getId().longValue());
    }

    @AfterEach
    void cleanup() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        carRepository.deleteAll();
    }
}
