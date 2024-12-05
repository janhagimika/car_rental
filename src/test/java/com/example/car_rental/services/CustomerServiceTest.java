package com.example.car_rental.services;

import com.example.car_rental.models.Customer;
import com.example.car_rental.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstname("John");
        customer1.setSurname("Doe");
        customer1.setEmailAddress("john.doe@example.com");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstname("Jane");
        customer2.setSurname("Doe");
        customer2.setEmailAddress("jane.doe@example.com");
    }

    @AfterEach
    void cleanup() {
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = customerService.getAllCustomers();

        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerAndRentalHistory() {
        when(customerRepository.findByIdWithRentals(1L)).thenReturn(Optional.of(customer1));

        Customer customer = customerService.getCustomerAndRentalHistory(1L);

        assertNotNull(customer);
        assertEquals("John", customer.getFirstname());
        verify(customerRepository, times(1)).findByIdWithRentals(1L);
    }

    @Test
    void testGetCustomerAndRentalHistoryWhenNotFound() {
        when(customerRepository.findByIdWithRentals(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> customerService.getCustomerAndRentalHistory(99L));
        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findByIdWithRentals(99L);
    }

    @Test
    void testSaveCustomer() {
        when(customerRepository.existsByEmailAddress(customer1.getEmailAddress())).thenReturn(false);
        when(customerRepository.save(customer1)).thenReturn(customer1);

        Customer savedCustomer = customerService.saveCustomer(customer1);

        assertNotNull(savedCustomer);
        assertEquals("John", savedCustomer.getFirstname());
        verify(customerRepository, times(1)).existsByEmailAddress(customer1.getEmailAddress());
        verify(customerRepository, times(1)).save(customer1);
    }

    @Test
    void testSaveCustomerWhenEmailAlreadyExists() {
        when(customerRepository.existsByEmailAddress(customer1.getEmailAddress())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(customer1));
        assertEquals("Email address already in use", exception.getMessage());

        verify(customerRepository, times(1)).existsByEmailAddress(customer1.getEmailAddress());
        verify(customerRepository, never()).save(any());
    }
}
