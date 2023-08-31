package com.example.food.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @NotBlank(message = "Menu Type cannot be blank")
    @Field(CollectionConstants.MENU_TYPE)
    private String menuType;

    @NotNull(message = "Menu Item Id cannot be null")
    @Field(CollectionConstants.MENU_ITEM_ID)
    private Integer menuItemId;

    @NotBlank(message = "Menu Item cannot be blank")
    @Field(CollectionConstants.MENU_ITEM)
    private String menuItem;

    @NotNull(message = "Quantity cannot be null")
    @Field(CollectionConstants.QUANTITY)
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @Field(CollectionConstants.PRICE)
    private Double price;
}
