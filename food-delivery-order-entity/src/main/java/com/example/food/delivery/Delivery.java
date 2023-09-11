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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CollectionConstants.DELIVERY_COLLECTION_NAME)
public class Delivery {
    @Transient
    public static final String SEQUENCE_NAME = "delivery_sequence";

    @Id
    @Field(CollectionConstants.DELIVERY_ID)
    private long id;

    @NotNull(message = "Order Id cannot be null")
    @Field(CollectionConstants.ORDER_ID)
    private long orderId;

    @NotBlank(message = "Delivery Person Id cannot be blank")
    @Field(CollectionConstants.DELIVERY_PERSON_ID)
    private String deliveryPersonId;

    @NotNull(message = "Delivery Status should be provided")
    @Field(CollectionConstants.IS_DELIVERED)
    private Boolean isDelivered;

    @Field(CollectionConstants.DELIVERY_OTP)
    private String otp;
}
