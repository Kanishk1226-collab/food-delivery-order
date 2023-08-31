package com.example.food.delivery.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailRequest {
    @NotBlank(message = "Customer Name cannot be blank")
    private String customerName;

    @NotBlank(message = "Customer Email cannot be blank")
    private String customerEmail;

    @NotBlank(message = "Customer Phone cannot be blank")
    private String customerPhone;

    @NotBlank(message = "Customer Location cannot be blank")
    private String customerLocation;
}
