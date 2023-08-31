package com.example.food.delivery.Request;

import com.example.food.delivery.Enums.OrderStatus;
import com.example.food.delivery.Validator.EnumNamePattern;
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
public class OrderRequest {
    @NotBlank(message = "Cart Id cannot be blank")
    private String cartId;

    @NotBlank(message = "Restaurant name cannot be blank")
    private String restaurantName;

    @NotNull(message = "Amount value should not be null")
    private Double totalAmount;

    @EnumNamePattern(
            regexp = "PLACED|PENDING|COMPLETED",
            message = "Order Status Should be either PLACED or PENDING or COMPLETED"
    )
    private OrderStatus orderStatus;

    private List<OrderItemRequest> orderItem;

    private CustomerDetailRequest customerDetail;

    private PaymentDetailRequest paymentDetail;
}
