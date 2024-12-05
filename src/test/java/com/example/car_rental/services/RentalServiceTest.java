package com.example.car_rental.services;

import com.example.car_rental.models.*;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Rental rental;
    private Car car;
    private Customer customer;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        car = new Car();
        car.setId(1L);
        car.setAvailable(true);

        customer = new Customer();
        customer.setId(1L);

        rental = new Rental();
        rental.setId(1L);
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setStatus(RentalStatus.PENDING);
    }

    @AfterEach
    void cleanup() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    void testCreateRentalSuccess() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental createdRental = rentalService.createRental(rental);

        assertNotNull(createdRental);
        assertEquals(RentalStatus.PENDING, createdRental.getStatus());
        verify(carRepository, times(1)).saveAndFlush(car);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void testCreateRentalCarNotAvailable() {
        car.setAvailable(false);
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Exception exception = assertThrows(RuntimeException.class, () -> rentalService.createRental(rental));
        assertEquals("Car is not available for rental", exception.getMessage());

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void testCreateRentalCarNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> rentalService.createRental(rental));
        assertEquals("Car not found with ID: 1", exception.getMessage());

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void testCreateRentalCustomerNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> rentalService.createRental(rental));
        assertEquals("Customer not found with ID: 1", exception.getMessage());

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void testCompleteRentalSuccess() {
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));
        when(rentalRepository.saveAndFlush(rental)).thenReturn(rental);

        Rental completedRental = rentalService.completeRental(rental.getId(), ReturnCondition.GOOD);

        assertNotNull(completedRental);
        assertEquals(RentalStatus.COMPLETED, completedRental.getStatus());
        assertEquals(ReturnCondition.GOOD, completedRental.getConditionOnReturn());
        verify(carRepository, times(1)).saveAndFlush(car);
        verify(rentalRepository, times(1)).saveAndFlush(rental);
    }

    @Test
    void testCompleteRentalCarInfoMissing() {
        rental.setCar(null);
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> rentalService.completeRental(rental.getId(), ReturnCondition.GOOD));
        assertEquals("Car information is missing", exception.getMessage());

        verify(carRepository, never()).saveAndFlush(any());
    }

    @Test
    void testCompleteRentalCustomerInfoMissing() {
        rental.setCustomer(null);
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> rentalService.completeRental(rental.getId(), ReturnCondition.GOOD));
        assertEquals("Customer information is missing", exception.getMessage());

        verify(carRepository, never()).saveAndFlush(any());
    }

    @Test
    void testCompleteRentalCarUnusable() {
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));
        when(rentalRepository.saveAndFlush(rental)).thenReturn(rental);

        Rental completedRental = rentalService.completeRental(rental.getId(), ReturnCondition.UNUSABLE);

        assertNotNull(completedRental);
        assertEquals(RentalStatus.COMPLETED, completedRental.getStatus());
        assertEquals(ReturnCondition.UNUSABLE, completedRental.getConditionOnReturn());
        assertFalse(car.isAvailable());
        verify(carRepository, times(1)).saveAndFlush(car);
    }
}
