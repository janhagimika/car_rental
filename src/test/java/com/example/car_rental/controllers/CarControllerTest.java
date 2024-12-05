package com.example.car_rental.controllers;

import com.example.car_rental.models.Car;
import com.example.car_rental.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setId(1L);
        car1.setBrand("Toyota");
        car1.setModel("Camry");
        car1.setYearOfManufacture(2020);
        car1.setColor("White");
        car1.setMileage(15000);
        car1.setAvailable(true);

        car2 = new Car();
        car2.setId(2L);
        car2.setBrand("Honda");
        car2.setModel("Civic");
        car2.setYearOfManufacture(2019);
        car2.setColor("Black");
        car2.setMileage(20000);
        car2.setAvailable(true);
    }

    @Test
    void testGetAllCars() {
        // Arrange
        List<Car> cars = Arrays.asList(car1, car2);
        when(carService.getAllCars()).thenReturn(cars);

        // Act
        ResponseEntity<List<Car>> response = carController.getAllCars();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        assertEquals("Toyota", response.getBody().get(0).getBrand());
        assertEquals("Honda", response.getBody().get(1).getBrand());
    }

    @Test
    void testGetAllCarsWhenNoneExist() {
        // Arrange
        when(carService.getAllCars()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Car>> response = carController.getAllCars();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetAvailableCars() {
        // Arrange
        List<Car> availableCars = Arrays.asList(car1);
        when(carService.getAvailableCars()).thenReturn(availableCars);

        // Act
        ResponseEntity<List<Car>> response = carController.getAvailableCars();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Toyota", response.getBody().get(0).getBrand());
    }

    @Test
    void testSaveCar() {
        // Arrange
        when(carService.saveCar(any(Car.class))).thenReturn(car1);

        // Act
        ResponseEntity<Car> response = carController.saveCar(car1);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals("Toyota", response.getBody().getBrand());
    }

    @Test
    void testSaveCarWhenInvalid() {
        // Arrange
        Car invalidCar = new Car(); // Missing required fields
        when(carService.saveCar(any(Car.class))).thenThrow(new IllegalArgumentException("Invalid car details"));

        // Act
        try {
            carController.saveCar(invalidCar);
        } catch (IllegalArgumentException ex) {
            // Assert
            assertEquals("Invalid car details", ex.getMessage());
        }
    }

    @Test
    void testUpdateCar() {
        // Arrange
        Car updatedCar = new Car();
        updatedCar.setId(1L);
        updatedCar.setBrand("Toyota");
        updatedCar.setModel("Corolla");
        updatedCar.setYearOfManufacture(2021);
        updatedCar.setColor("Blue");
        updatedCar.setMileage(10000);
        updatedCar.setAvailable(true);

        when(carService.updateCar(eq(1L), any(Car.class))).thenReturn(updatedCar);

        // Act
        Car response = carController.updateCar(1L, updatedCar);

        // Assert
        assertEquals("Toyota", response.getBrand());
        assertEquals("Corolla", response.getModel());
        assertEquals(2021, response.getYearOfManufacture());
        assertEquals("Blue", response.getColor());
        assertEquals(10000, response.getMileage());
    }

    @Test
    void testUpdateCarWhenNotFound() {
        // Arrange
        when(carService.updateCar(eq(99L), any(Car.class))).thenThrow(new RuntimeException("Car not found"));

        // Act
        try {
            carController.updateCar(99L, car1);
        } catch (RuntimeException ex) {
            // Assert
            assertEquals("Car not found", ex.getMessage());
        }
    }

    @Test
    void testDeleteCar() {
        // Act
        ResponseEntity<String> response = carController.deleteCar(1L);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals("Car deleted successfully!", response.getBody());

        // Verify the service method was called
        verify(carService, times(1)).deleteCar(1L);
    }

    @Test
    void testDeleteCarWhenNotFound() {
        // Arrange
        doThrow(new RuntimeException("Car not found")).when(carService).deleteCar(99L);

        // Act
        try {
            carController.deleteCar(99L);
        } catch (RuntimeException ex) {
            // Assert
            assertEquals("Car not found", ex.getMessage());
        }
    }
}
