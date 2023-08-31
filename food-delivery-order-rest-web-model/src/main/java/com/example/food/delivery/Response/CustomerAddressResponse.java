package com.example.food.delivery.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAddressResponse {
    private String custName;

    private String custEmail;
    private String custPhone;
    private Integer doorNo;

    private String locality;

    private String city;

    private String pincode;
}
