package com.example.car_rental.controllers;

import com.example.car_rental.mappers.CustomerMapper;
import com.example.car_rental.models.Customer;
import com.example.car_rental.models.CustomerDTO;
import com.example.car_rental.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "API for managing customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of customers.")
    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(CustomerMapper::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get customer details with rental history", description = "Retrieve a customer's details along with their rental history.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the customer's details.")
    @ApiResponse(responseCode = "404", description = "Customer not found.")
    @GetMapping("/{id}")
    public Customer getCustomerAndRentalHistory(@PathVariable Long id) {
        return customerService.getCustomerAndRentalHistory(id);
    }

    @Operation(
            summary = "Add a new customer",
            description = "Add a new customer to the system by providing their details."
    )
    @ApiResponse(responseCode = "201", description = "Customer added successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid customer details provided.")
    @PostMapping
    public ResponseEntity<Customer> saveCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer details to be added. Only `firstname`, `surname`, `emailAddress`, `phoneNumber` and `address` is required.",
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = "{\n  \"firstname\": \"John\",\n  \"surname\": \"Doe\",\n  \"emailAddress\": \"john.doe@example.com\",\n  \"phoneNumber\": \"123456789\",\n  \"address\": \"123 Main St, Springfield\"\n}"
                            )
                    )
            )
            @Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCustomer);
    }


}
