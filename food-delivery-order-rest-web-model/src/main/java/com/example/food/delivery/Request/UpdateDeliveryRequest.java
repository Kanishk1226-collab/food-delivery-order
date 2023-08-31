package com.example.food.delivery.Request;

import com.example.food.delivery.Validator.ValidBooleanValue;
import jakarta.validation.constraints.Email;
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
public class UpdateDeliveryRequest {

    @NotNull(message = "Delivery Id cannot be blank")
    private Integer deliveryId;

    @NotBlank(message = "Delivery Person Id cannot be blank")
    @Email(message = "Enter valid Delivery Person Id")
    private String deliveryPersonId;
}
