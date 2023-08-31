package com.example.food.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAgentDetail {
    @Field("delivery_agent_name")
    private String delAgentName;

    @Field("delivery_agent_email")
    private String delAgentEmail;

    @Field("delivery_agent_phone")
    private String delAgentPhone;
}