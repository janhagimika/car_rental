package com.example.car_rental.repositories;

import com.example.car_rental.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.rentalHistory WHERE c.id = :id")
    Optional<Customer> findByIdWithRentals(@Param("id") Long id);

}
