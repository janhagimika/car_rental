package com.example.car_rental.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a rental.")
public enum RentalStatus {
    @Schema(description = "The rental is pending and not yet completed.")
    PENDING,

    @Schema(description = "The rental is completed.")
    COMPLETED
}

