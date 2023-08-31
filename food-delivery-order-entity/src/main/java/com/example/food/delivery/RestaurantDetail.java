package com.example.food.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetail {
    @Field(CollectionConstants.RESTAURANT_NAME)
    private String restName;

    @Field(CollectionConstants.REST_AGENT_EMAIL)
    private String restAgentEmail;

    @Field(CollectionConstants.LOCATION)
    private String location;

    @Field(CollectionConstants.AVG_RATING)
    private int avgRating;

    @Field(CollectionConstants.REST_IS_VEG)
    private boolean isVeg;
}
