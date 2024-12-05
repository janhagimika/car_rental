package com.example.car_rental.services;

import com.example.car_rental.models.Customer;
import com.example.car_rental.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerAndRentalHistory(Long id) {
        return customerRepository.findByIdWithRentals(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
    }

    public Customer saveCustomer(Customer customer) {
        if (customerRepository.existsByEmailAddress(customer.getEmailAddress())) {
            throw new IllegalArgumentException("Email address already in use");
        }
        return customerRepository.save(customer);
    }

}
