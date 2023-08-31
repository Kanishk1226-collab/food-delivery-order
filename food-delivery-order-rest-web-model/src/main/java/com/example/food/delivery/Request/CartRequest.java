package com.example.food.delivery.Request;

import jakarta.validation.constraints.Email;
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
public class CartRequest {
    @NotBlank(message = "Cart Id cannot be blank")
    @Email(message = "Enter Valid Email Format")
    private String cartId;
}
