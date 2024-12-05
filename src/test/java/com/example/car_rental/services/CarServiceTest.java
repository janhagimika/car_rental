package com.example.car_rental.services;

import com.example.car_rental.models.Car;
import com.example.car_rental.repositories.CarRepository;
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

class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    private Car car1;
    private Car car2;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        car1 = new Car();
        car1.setId(1L);
        car1.setBrand("Toyota");
        car1.setModel("Camry");
        car1.setYearOfManufacture(2020);
        car1.setColor("White");
        car1.setMileage(15000);
        car1.setAvailable(true);
        car1.setVersion(1);

        car2 = new Car();
        car2.setId(2L);
        car2.setBrand("Honda");
        car2.setModel("Civic");
        car2.setYearOfManufacture(2019);
        car2.setColor("Black");
        car2.setMileage(20000);
        car2.setAvailable(false);
        car2.setVersion(1);
    }

    @AfterEach
    void cleanup() {
        carRepository.deleteAll();
    }

    @Test
    void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carService.getAllCars();

        assertEquals(2, cars.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testSaveCar() {
        when(carRepository.save(car1)).thenReturn(car1);

        Car savedCar = carService.saveCar(car1);

        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getBrand());
        verify(carRepository, times(1)).save(car1);
    }

    @Test
    void testSaveCarWhenNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> carService.saveCar(null));
        assertEquals("Car cannot be null", exception.getMessage());

        verify(carRepository, never()).save(any());
    }

    @Test
    void testDeleteCar() {
        doNothing().when(carRepository).deleteById(1L);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCarWhenNotFound() {
        doThrow(new NoSuchElementException("Car not found with ID: 99")).when(carRepository).deleteById(99L);

        Exception exception = assertThrows(NoSuchElementException.class, () -> carService.deleteCar(99L));
        assertEquals("Car not found with ID: 99", exception.getMessage());

        verify(carRepository, times(1)).deleteById(99L);
    }

    @Test
    void testGetAvailableCars() {
        when(carRepository.findByIsAvailableTrue()).thenReturn(Arrays.asList(car1));

        List<Car> availableCars = carService.getAvailableCars();

        assertEquals(1, availableCars.size());
        assertTrue(availableCars.get(0).isAvailable());
        verify(carRepository, times(1)).findByIsAvailableTrue();
    }

    @Test
    void testGetAvailableCarsWhenNoneAvailable() {
        when(carRepository.findByIsAvailableTrue()).thenReturn(List.of());

        List<Car> availableCars = carService.getAvailableCars();

        assertTrue(availableCars.isEmpty());
        verify(carRepository, times(1)).findByIsAvailableTrue();
    }

    @Test
    void testUpdateCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
        when(carRepository.save(car1)).thenReturn(car1);

        Car updatedCar = new Car();
        updatedCar.setId(1L);
        updatedCar.setBrand("Ford");
        updatedCar.setModel("Focus");
        updatedCar.setYearOfManufacture(2021);
        updatedCar.setColor("Blue");
        updatedCar.setMileage(5000);
        updatedCar.setAvailable(true);
        updatedCar.setVersion(1);

        Car result = carService.updateCar(1L, updatedCar);

        // Assertions
        assertEquals("Ford", result.getBrand());
        assertEquals("Focus", result.getModel());
        assertEquals(2021, result.getYearOfManufacture());
        assertEquals("Blue", result.getColor());
        assertEquals(5000, result.getMileage());
        assertTrue(result.isAvailable());

        // Verifications
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(car1);
    }

    @Test
    void testUpdateCarWhenNotAvailable() {
        car1.setAvailable(false);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));

        Car updatedCar = new Car();
        updatedCar.setId(1L);
        updatedCar.setBrand("Honda");
        updatedCar.setModel("Civic");
        updatedCar.setYearOfManufacture(2019);
        updatedCar.setColor("Black");
        updatedCar.setMileage(20000);
        updatedCar.setAvailable(false);
        updatedCar.setVersion(1);

        Exception exception = assertThrows(IllegalStateException.class, () -> carService.updateCar(1L, updatedCar));
        assertEquals("Car is not available for updates", exception.getMessage());

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, never()).save(any());
    }

    @Test
    void testUpdateCarWhenNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> carService.updateCar(99L, car1));
        assertEquals("Car not found!", exception.getMessage());

        verify(carRepository, times(1)).findById(99L);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void testGetCarByIdWhenNotFound() {
        // Arrange
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            carRepository.findById(99L)
                    .orElseThrow(() -> new NoSuchElementException("Car not found with ID: " + 99L));
        });

        assertEquals("Car not found with ID: 99", exception.getMessage());
        verify(carRepository, times(1)).findById(99L);
    }

}
