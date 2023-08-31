package com.example.food.delivery.Response;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentDetailResponse {
    private String paymentMethod;
    private Boolean isPaid;
    private Boolean isOfflinePayment;
}
