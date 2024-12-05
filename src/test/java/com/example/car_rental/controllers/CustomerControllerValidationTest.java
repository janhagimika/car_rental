package com.example.car_rental.controllers;

import com.example.car_rental.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSaveCustomerValidationError() throws Exception {
        String invalidCustomerJson = """
    {
        "firstname": "",
        "surname": "",
        "emailAddress": "invalid-email",
        "address": "",
        "phoneNumber": ""
    }
    """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCustomerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstname").value("First name is required")) // Adjust to the actual message
                .andExpect(jsonPath("$.surname").value("Surname is required"))
                .andExpect(jsonPath("$.emailAddress").value("Invalid email address"))
                .andExpect(jsonPath("$.address").value("Address is required"))
                .andExpect(jsonPath("$.phoneNumber").value("Phone number is required"));
    }


}
