package com.example.car_rental.controllers;

import com.example.car_rental.models.*;
import com.example.car_rental.services.RentalService;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @InjectMocks
    private RentalController rentalController;

    @Mock
    private RentalService rentalService;

    private Customer customer;

    private Rental rental;

    @BeforeEach
    void setUp() {
        rental = new Rental();

        // Create and set Customer
        Customer customer = new Customer();
        customer.setId(1L);
        rental.setCustomer(customer);

        // Create and set Car
        Car car = new Car();
        car.setId(1L);
        rental.setCar(car);

        rental.setId(1L);
        rental.setStatus(RentalStatus.PENDING);
        rental.setRentalDate(LocalDateTime.now());
        rental.setPlannedReturnDate(LocalDateTime.now().plusDays(3));

        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest());
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void testCreateRental() {
        // Arrange
        when(rentalService.createRental(any(Rental.class))).thenReturn(rental);

        // Act
        ResponseEntity<Rental> response = rentalController.createRental(rental);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation(), "Location header should not be null");
        assertEquals(1L, response.getBody().getId());
        assertEquals(RentalStatus.PENDING, response.getBody().getStatus());
    }



    @Test
    void testCreateRentalWhenInvalid() {
        // Arrange
        Rental invalidRental = new Rental(); // Missing required fields
        when(rentalService.createRental(any(Rental.class))).thenThrow(new IllegalArgumentException("Invalid rental details"));

        // Act
        try {
            rentalController.createRental(invalidRental);
        } catch (IllegalArgumentException ex) {
            // Assert
            assertEquals("Invalid rental details", ex.getMessage());
        }
    }

    @Test
    void testReturnRental() {
        // Arrange
        rental.setStatus(RentalStatus.COMPLETED);
        rental.setConditionOnReturn(ReturnCondition.GOOD);
        when(rentalService.completeRental(eq(1L), any(ReturnCondition.class))).thenReturn(rental);

        // Act
        ResponseEntity<Rental> response = rentalController.returnRental(1L, ReturnCondition.GOOD);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(RentalStatus.COMPLETED, response.getBody().getStatus());
        assertEquals(ReturnCondition.GOOD, response.getBody().getConditionOnReturn());
    }

    @Test
    void testReturnRentalWhenNotFound() {
        // Arrange
        when(rentalService.completeRental(eq(99L), any(ReturnCondition.class))).thenThrow(new RuntimeException("Rental not found"));

        // Act
        try {
            rentalController.returnRental(99L, ReturnCondition.GOOD);
        } catch (RuntimeException ex) {
            // Assert
            assertEquals("Rental not found", ex.getMessage());
        }
    }

    @Test
    void testReturnRentalWhenInvalidCondition() {
        // Arrange
        when(rentalService.completeRental(eq(1L), any(ReturnCondition.class))).thenThrow(new IllegalArgumentException("Invalid return condition"));

        // Act
        try {
            rentalController.returnRental(1L, ReturnCondition.UNUSABLE);
        } catch (IllegalArgumentException ex) {
            // Assert
            assertEquals("Invalid return condition", ex.getMessage());
        }
    }
}
