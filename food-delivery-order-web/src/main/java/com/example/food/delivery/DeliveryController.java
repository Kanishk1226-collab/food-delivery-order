package com.example.food.delivery;

import com.example.food.delivery.Request.CartRequest;
import com.example.food.delivery.Request.DeliveryRequest;
import com.example.food.delivery.Request.UpdateDeliveryRequest;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @PostMapping(value = "/createDelivery")
    public ResponseEntity<BaseResponse<?>> createDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest){
        return deliveryService.createDelivery(deliveryRequest);
    }

    @GetMapping(value = "/getDeliveries")
    public ResponseEntity<BaseResponse<?>> getAllDelivery(@RequestParam(defaultValue = "0") int page){
        return deliveryService.getAllDelivery(page);
    }

    @GetMapping(value = "/deliveryByAgentId")
    public ResponseEntity<BaseResponse<?>> getDeliveryByDelAgent(@RequestParam(defaultValue = "0") int page, String deliveryPersonId){
        return deliveryService.getDeliveryByDelAgent(deliveryPersonId, page);
    }

    @PutMapping("/successDelivery")
    public ResponseEntity<BaseResponse<?>> updateDeliveryStatus(@Valid @RequestBody UpdateDeliveryRequest updateDeliveryRequest) {
        return deliveryService.updateDeliveryStatus(updateDeliveryRequest);
    }
}
