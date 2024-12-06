package com.example.car_rental.controllers;

import com.example.car_rental.mappers.CustomerMapper;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.CustomerDTO;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstname("John");
        customer1.setSurname("Doe");
        customer1.setEmailAddress("john.doe@example.com");
        customer1.setAddress("123 Main St");
        customer1.setPhoneNumber("123456789");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstname("Jane");
        customer2.setSurname("Doe");
        customer2.setEmailAddress("jane.doe@example.com");
        customer2.setAddress("456 Elm St");
        customer2.setPhoneNumber("987654321");

        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest());
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void cleanup() {
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act
        List<CustomerDTO> response = customerController.getAllCustomers();

        // Assert
        assertEquals(2, response.size());
        assertEquals("John", response.get(0).getFirstname());
        assertEquals("Jane", response.get(1).getFirstname());
    }

    @Test
    void testGetCustomerAndRentalHistory() {
        // Arrange
        when(customerService.getCustomerAndRentalHistory(1L)).thenReturn(customer1);

        // Act
        Customer response = customerController.getCustomerAndRentalHistory(1L);

        // Assert
        assertEquals("John", response.getFirstname());
        assertEquals("Doe", response.getSurname());
        assertEquals("john.doe@example.com", response.getEmailAddress());
    }

    @Test
    void testGetCustomerAndRentalHistoryWhenNotFound() {
        // Arrange
        when(customerService.getCustomerAndRentalHistory(99L)).thenThrow(new RuntimeException("Customer not found"));

        // Act & Assert
        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> customerController.getCustomerAndRentalHistory(99L));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer1);

        // Act
        ResponseEntity<Customer> response = customerController.saveCustomer(customer1);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation(), "Location header should not be null");
        assertEquals("John", response.getBody().getFirstname());
        assertEquals("Doe", response.getBody().getSurname());
        assertEquals("john.doe@example.com", response.getBody().getEmailAddress());
    }


    @Test
    void testSaveCustomerWhenInvalid() {
        // Arrange
        Customer invalidCustomer = new Customer(); // Missing required fields
        when(customerService.saveCustomer(any(Customer.class))).thenThrow(new IllegalArgumentException("Invalid customer details"));

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> customerController.saveCustomer(invalidCustomer));
        assertEquals("Invalid customer details", exception.getMessage());
    }
}
