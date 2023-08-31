package com.example.food.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CollectionConstants.CART_COLLECTION_NAME)
public class Cart {
    @Transient
    public static final String SEQUENCE_NAME = "carts_sequence";

    @Id
    @Field(CollectionConstants.ID)
    private long id;

    @NotBlank(message = "Cart Id cannot be blank")
    @Field(CollectionConstants.CART_ID)
    private String cartId;

    @NotNull(message = "Restaurant Id cannot be null")
    @Field(CollectionConstants.RESTAURANT_ID)
    private Integer restaurantId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Field(CollectionConstants.RESTAURANT_NAME)
    private String restaurantName;

    @NotNull(message = "Amount value should not be null")
    @Field(CollectionConstants.TOTAL_AMOUNT)
    private Double totalAmount;

    @Field(CollectionConstants.ORDER_ITEM_LIST)
    private List<OrderItem> orderItem;
}
