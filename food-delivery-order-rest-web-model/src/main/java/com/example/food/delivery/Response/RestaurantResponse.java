package com.example.food.delivery.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponse {

    private String restName;
    private String restAgentEmail;
    private String location;
    private int avgRating;
    private boolean isVeg;

}
