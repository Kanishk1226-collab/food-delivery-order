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
public class DeliveryRequest {
    @NotNull(message = "Order Id cannot be null")
    private Long orderId;

    @NotBlank(message = "Delivery Person Id cannot be blank")
    private String deliveryPersonId;

    @ValidBooleanValue(message = "Invalid Delivery Status value")
    @NotNull(message = "Delivery Status should be provided")
    private Boolean isDelivered;
}
