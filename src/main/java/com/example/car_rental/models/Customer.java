package com.example.car_rental.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Schema(description = "Represents a customer in the rental system.")
@Table(
        indexes = {
                @Index(name = "idx_email_address", columnList = "emailAddress", unique = true)
        }
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the customer.", example = "John")
    private String firstname;

    @NotBlank(message = "Surname is required")
    @Schema(description = "Last name of the customer.", example = "Doe")
    private String surname;

    @NotBlank(message = "Address is required")
    @Schema(description = "Address of the customer.", example = "123 Main St, Springfield")
    private String address;

    @Email(message = "Invalid email address")
    @Schema(description = "Email address of the customer.", example = "john.doe@example.com")
    private String emailAddress;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "Phone number of the customer.", example = "123456789")
    private String phoneNumber;

    @JsonManagedReference
    @Schema(description = "History of rentals made by the customer. Included in detailed views.")
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rental> rentalHistory;
}
