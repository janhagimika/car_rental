package com.example.car_rental.controllers;

import com.example.car_rental.mappers.CustomerMapper;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.CustomerDTO;
import com.example.car_rental.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(CustomerMapper::toCustomerDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public Customer getCustomerAndRentalHistory(@PathVariable Long id) {
        return customerService.getCustomerAndRentalHistory(id);
    }

    @PostMapping
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

}
