package com.example.food.delivery.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartRequest {
    @NotNull(message = "Cart Id cannot be blank")
    private String cartId;

    @NotNull(message = "Menu Item Id cannot be null")
    @Positive(message = "Menu Item Id should be greater than zero")
    private Integer menuItemId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity should be greater than zero")
    private Integer quantity;

//    @NotNull(message = "Restaurant Id cannot be null")
//    private Integer restaurantId;
//
//    @NotBlank(message = "Restaurant name cannot be blank")
//    private String restaurantName;
//
//    private List<OrderItemRequest> orderItem;
}
