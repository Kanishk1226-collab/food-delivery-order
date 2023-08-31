package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentRequest {
    @NotNull(message = "Is paid must be provided")
    @ValidBooleanValue(message = "Invalid Is Paid value")
    private Boolean isPaid;

    @NotBlank(message = "Cart Id cannot be blank")
    private String cartId;
}
