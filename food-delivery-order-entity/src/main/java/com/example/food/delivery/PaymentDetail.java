package com.example.food.delivery;

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
public class PaymentDetail {
    @NotBlank(message = "Payment Method cannot be blank")
    @Field(CollectionConstants.PAYMENT_METHOD)
    private String paymentMethod;

    @NotNull(message = "Is paid must be provided")
    @Field(CollectionConstants.IS_PAID)
    private Boolean isPaid;

    @NotNull(message = "Is Offline Payment must be provided")
    @Field(CollectionConstants.IS_OFFLINE_PAYMENT)
    private Boolean isOfflinePayment;
}
