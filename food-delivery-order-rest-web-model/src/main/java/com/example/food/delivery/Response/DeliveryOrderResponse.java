package com.example.food.delivery.Response;

import com.example.food.delivery.Enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderResponse {
    private long orderId;
    private Boolean isDelivered;
    private Date orderDate;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemResponse> orderItem;
    private CustomerDetailResponse customerDetailResponse;
}
