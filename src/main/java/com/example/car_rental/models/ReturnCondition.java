package com.example.car_rental.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Condition of the car upon return.")
public enum ReturnCondition {
    @Schema(description = "Car returned in excellent condition.")
    EXCELLENT,

    @Schema(description = "Car returned in good condition with minor issues.")
    GOOD,

    @Schema(description = "Car returned with visible damage.")
    DAMAGED,

    @Schema(description = "Car returned in an unusable state.")
    UNUSABLE
}

