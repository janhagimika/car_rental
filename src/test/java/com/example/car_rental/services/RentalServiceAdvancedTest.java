package com.example.car_rental.services;

import com.example.car_rental.models.Car;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.Rental;
import com.example.car_rental.repositories.CarRepository;
import com.example.car_rental.repositories.CustomerRepository;
import com.example.car_rental.repositories.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RentalServiceAdvancedTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Car car;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Persist Car with all required fields
        car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Red");
        car.setAvailable(true);
        car = carRepository.save(car); // Persist and make it managed

        // Persist Customer with all required fields
        customer = new Customer();
        customer.setFirstname("John");
        customer.setSurname("Doe");
        customer.setAddress("123 Main Street, Springfield");
        customer.setPhoneNumber("123-456-7890");
        customer = customerRepository.save(customer); // Persist and make it managed
    }


    void cleanup() {
        rentalRepository.deleteAll();
        customerRepository.deleteAll();
        carRepository.deleteAll();
    }

    private Rental createRental() {
        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalDate(LocalDateTime.now());
        rental.setReturnDate(LocalDateTime.now().plusDays(5));
        return rentalRepository.save(rental); // Save and return managed entity
    }

    @Test
    @Transactional
    void simulateRollback() {
        Rental rental = createRental();

        try {
            rentalRepository.save(rental);
            throw new RuntimeException("Simulated error for rollback");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    void verifyRollback() {
        assertTrue(rentalRepository.findAll().isEmpty(), "Database should be empty after rollback");
    }




    @Test
    void testOptimisticLocking() {
        // Given
        Rental rental = createRental();

        // Simulate concurrent updates
        Rental rental1 = rentalRepository.findById(rental.getId()).orElseThrow();
        Rental rental2 = rentalRepository.findById(rental.getId()).orElseThrow();

        rental1.setReturnDate(LocalDateTime.now().plusDays(7));
        rentalRepository.save(rental1); // First save succeeds

        rental2.setReturnDate(LocalDateTime.now().plusDays(10));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> rentalRepository.save(rental2),
                "Expected optimistic locking exception for concurrent updates");
        cleanup();
    }

    @Test
    void testConcurrentUpdatesWithExecutorService() throws InterruptedException {
        // Given
        Rental rental = createRental();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        final Rental finalRental = rental;

        // Use hardcoded timestamps
        final LocalDateTime task1ReturnDate = LocalDateTime.of(2024, 12, 12, 17, 30, 0);
        final LocalDateTime task2ReturnDate = LocalDateTime.of(2024, 12, 15, 17, 30, 0);

        // Latch to coordinate tasks
        CountDownLatch latch = new CountDownLatch(1);

        Runnable task1 = () -> {
            Rental rental1 = rentalRepository.findById(finalRental.getId()).orElseThrow();
            rental1.setReturnDate(task1ReturnDate);
            rentalRepository.save(rental1); // Task1 succeeds
            latch.countDown(); // Signal Task2 to proceed
        };

        Runnable task2 = () -> {
            try {
                latch.await(); // Wait for Task1 to finish
                Rental rental2 = rentalRepository.findById(finalRental.getId()).orElseThrow();
                rental2.setReturnDate(task2ReturnDate);
                rentalRepository.save(rental2); // This should fail due to optimistic locking
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Optimistic locking failed for task2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        executorService.submit(task1);
        executorService.submit(task2);
        executorService.shutdown();

        // Wait for tasks to complete
        while (!executorService.isTerminated()) {
            Thread.sleep(100); // Check completion every 100ms
        }

        // Verify final state
        Rental finalRentalResult = rentalRepository.findById(finalRental.getId()).orElseThrow();
        assertEquals(task2ReturnDate, finalRentalResult.getReturnDate(),
                "Task1 should have succeeded, and Task2 should have failed due to optimistic locking");
        cleanup();
    }




}
