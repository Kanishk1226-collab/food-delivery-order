package com.example.food.delivery;

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
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CollectionConstants.ORDER_COLLECTION_NAME)
public class Order {
    @Transient
    public static final String SEQUENCE_NAME = "orders_sequence";

    @Id
    @Field(CollectionConstants.ORDER_ID)
    private long id;

    @NotBlank(message = "Cart Id cannot be blank")
    @Field(CollectionConstants.CART_ID)
    private String cartId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Field(CollectionConstants.RESTAURANT_NAME)
    private String restaurantName;

    @CreatedDate
    @Field(CollectionConstants.ORDER_DATE)
    private Date orderDate;

    @Version
    private Long version;

    @NotNull(message = "Amount value should not be null")
    @Field(CollectionConstants.TOTAL_AMOUNT)
    private Double totalAmount;

    @Field(CollectionConstants.ORDER_STATUS)
    private OrderStatus orderStatus;

    @Field(CollectionConstants.ORDER_ITEM_LIST)
    private List<OrderItem> orderItem;

    @Field(CollectionConstants.CUSTOMER_DETAIL_LIST)
    private CustomerDetail customerDetail;

    @Field(CollectionConstants.PAYMENT_DETAIL_LIST)
    private PaymentDetail paymentDetail;

    @Field(CollectionConstants.REST_DETAIL_LIST)
    private RestaurantDetail restaurantDetail;

    @Field("delivery_agent_detail")
    private DeliveryAgentDetail deliveryAgentDetail;
}
