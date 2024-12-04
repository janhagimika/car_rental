package com.example.car_rental.controllers;

import com.example.car_rental.models.Customer;
import com.example.car_rental.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerAndRentalHistory(@PathVariable Long id) {
        return customerService.getCustomerAndRentalHistory(id);
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

}
