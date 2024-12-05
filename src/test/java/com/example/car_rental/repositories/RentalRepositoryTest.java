package com.example.car_rental.repositories;

import com.example.car_rental.models.Car;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.Rental;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Rental rental;
    private Car car;
    private Customer customer;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Red");
        car.setAvailable(true);
        car = carRepository.save(car);

        customer = new Customer();
        customer.setFirstname("Jane");
        customer.setSurname("Smith");
        customer.setAddress("456 Main St");
        customer.setPhoneNumber("987654321");
        customer = customerRepository.save(customer);

        rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalDate(LocalDateTime.now());
        rental.setReturnDate(LocalDateTime.now().plusDays(5));
        rental = rentalRepository.save(rental);
    }

    @Test
    void testSaveAndFindById() {
        Optional<Rental> found = rentalRepository.findById(rental.getId());
        assertTrue(found.isPresent(), "Rental should be found");
        assertEquals(customer.getFirstname(), found.get().getCustomer().getFirstname(),
                "Customer should match");
    }

    @Test
    void testDelete() {
        rentalRepository.delete(rental);
        assertFalse(rentalRepository.findById(rental.getId()).isPresent(),
                "Rental should be deleted");
    }

    @AfterEach
    void cleanup() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        carRepository.deleteAll();
    }
}
