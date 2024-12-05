package com.example.car_rental.repositories;

import com.example.car_rental.models.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setFirstname("John");
        customer.setSurname("Doe");
        customer.setAddress("123 Main St");
        customer.setPhoneNumber("123456789");
        customer.setEmailAddress("john.doe@example.com");
        customer = customerRepository.save(customer);
    }

    @Test
    void testExistsByEmailAddress() {
        assertTrue(customerRepository.existsByEmailAddress("john.doe@example.com"),
                "Customer with email should exist");
        assertFalse(customerRepository.existsByEmailAddress("nonexistent@example.com"),
                "Customer with email should not exist");
    }

    @Test
    void testFindByIdWithRentals() {
        Optional<Customer> result = customerRepository.findByIdWithRentals(customer.getId());
        assertTrue(result.isPresent(), "Customer should be found");
        assertEquals(customer.getEmailAddress(), result.get().getEmailAddress(),
                "Email should match");
    }

    @AfterEach
    void cleanup() {
        customerRepository.deleteAll();
    }
}
