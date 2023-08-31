package com.example.food.delivery;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetail {
    @NotBlank(message = "Customer Name cannot be blank")
    @Field(CollectionConstants.CUSTOMER_NAME)
    private String customerName;

    @NotBlank(message = "Customer Email cannot be blank")
    @Field(CollectionConstants.CUSTOMER_EMAIL)
    private String customerEmail;

    @NotBlank(message = "Customer Phone cannot be blank")
    @Field(CollectionConstants.CUSTOMER_PHONE)
    private String customerPhone;

    @Field(CollectionConstants.CUSTOMER_DOOR_NO)
    private Integer doorNo;

    @Field(CollectionConstants.CUSTOMER_LOCALITY)
    private String locality;

    @Field(CollectionConstants.CUSTOMER_CITY)
    private String city;

    @Field(CollectionConstants.PIN_CODE)
    private String pincode;
}
