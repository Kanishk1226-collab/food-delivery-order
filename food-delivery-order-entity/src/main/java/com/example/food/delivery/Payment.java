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
@Document(collection = "payment")
public class Payment {
    @Transient
    public static final String SEQUENCE_NAME = "payment_sequence";

    @Id
    @Field("payment_id")
    private long id;

    @NotBlank(message = "Payment Method cannot be blank")
    @Field(CollectionConstants.PAYMENT_METHOD)
    private String paymentMethod;

    @NotBlank(message = "Payment Description cannot be blank")
    @Field("payment_description")
    private String paymentDescription;

    @NotNull(message = "Is Offline Payment must be provided")
    @Field(CollectionConstants.IS_OFFLINE_PAYMENT)
    private Boolean isOfflinePayment;
}
