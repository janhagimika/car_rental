package com.example.car_rental.models;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String firstname;
    private String surname;
    private String emailAddress;
    private String phoneNumber;

}

