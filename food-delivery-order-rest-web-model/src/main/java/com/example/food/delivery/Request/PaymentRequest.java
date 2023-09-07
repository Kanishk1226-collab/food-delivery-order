package com.example.food.delivery.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class PaymentRequest {
    @NotBlank(message = "Payment Method cannot be blank")
    private String paymentMethod;

    @NotBlank(message = "Payment Description cannot be blank")
    private String paymentDescription;

    @NotNull(message = "Is Offline Payment must be provided")
    private Boolean isOfflinePayment;
}
