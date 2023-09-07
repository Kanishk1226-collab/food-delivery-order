package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.DeliveryRequest;
import com.example.food.delivery.Request.UpdateDeliveryRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface DeliveryService {
    ResponseEntity<BaseResponse<?>> createDelivery(DeliveryRequest deliveryRequest);
    ResponseEntity<BaseResponse<?>> getAllDelivery(int page);
    ResponseEntity<BaseResponse<?>> getDeliveryByDelAgent(String delAgentEmail, int page);
    ResponseEntity<BaseResponse<?>> updateDeliveryStatus(UpdateDeliveryRequest updateDeliveryRequest);

}
