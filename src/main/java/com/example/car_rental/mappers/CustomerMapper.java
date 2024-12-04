package com.example.car_rental.mappers;

import com.example.car_rental.models.Customer;
import com.example.car_rental.models.CustomerDTO;

public class CustomerMapper {

    public static CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstname(customer.getFirstname());
        dto.setSurname(customer.getSurname());
        dto.setEmailAddress(customer.getEmailAddress());
        dto.setPhoneNumber(customer.getPhoneNumber());
        return dto;
    }
}

