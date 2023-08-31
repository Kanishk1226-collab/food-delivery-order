package com.example.food.delivery.Request;

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
public class OrderItemRequest {
    @NotBlank(message = "Menu Type cannot be blank")
    private String menuType;

    @NotNull(message = "Menu Item Id cannot be null")
    private Integer menuItemId;

    @NotBlank(message = "Menu Item cannot be blank")
    private String menuItem;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    private Double price;
}
