package edu.pict.dbmsbackend.dto;

import edu.pict.dbmsbackend.model.enums.CarStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1990, message = "Year must be 1990 or later")
    @Max(value = 2024, message = "Year must be 2024 or earlier")
    private Integer year;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;

    private CarStatus status;

    @NotBlank(message = "Image URL is required")
    private String image;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private List<String> features;

    @NotNull(message = "Mileage is required")
    @Min(value = 0, message = "Mileage must be 0 or greater")
    private Integer mileage;

    private List<String> issues;
}
