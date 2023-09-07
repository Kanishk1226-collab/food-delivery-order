package com.example.food.delivery.Request;

import com.example.food.delivery.Enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class OrderFilter {
    @NotBlank(message = "Cart Id cannot be blank")
    private String cartId;

    private OrderStatus orderStatus;
}
