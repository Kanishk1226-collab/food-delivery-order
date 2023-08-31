package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailRequest {
    @NotBlank(message = "Payment Method cannot be blank")
    private String paymentMethod;

    @NotNull(message = "Is paid must be provided")
    @ValidBooleanValue(message = "Invalid Is Paid value")
    private Boolean isPaid;

    @NotNull(message = "Is Offline Payment must be provided")
    @ValidBooleanValue(message = "Invalid Is Offline Payment value")
    private Boolean isOfflinePayment;
}
