package com.example.food.delivery;

import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @GetMapping(value = "/all")
    @PreAuthorize("#userRole == 'ADMIN' or #userRole == 'CO_ADMIN' or #userRole == 'SUPER_ADMIN'")
    public ResponseEntity<BaseResponse<?>> getAllDelivery(@RequestParam(defaultValue = "0") int page){
        return deliveryService.getAllDelivery(page);
    }

    @GetMapping(value = "/view")
    @PreAuthorize("#userRole == 'DELIVERY_AGENT'")
    public ResponseEntity<BaseResponse<?>> getDeliveryByDelAgent(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestHeader("userEmail") String userEmail,
                                                                 @RequestHeader("userRole") String userRole){
        return deliveryService.getDeliveryByDelAgent(userEmail, page);
    }

    @PutMapping("/status/update")
    @PreAuthorize("#userRole == 'DELIVERY_AGENT'")
    public ResponseEntity<BaseResponse<?>> updateDeliveryStatus(@Valid @RequestParam int deliveryId,
                                                                @RequestParam String otp,
                                                                @RequestHeader("userEmail") String userEmail,
                                                                @RequestHeader("userRole") String userRole) {
        return deliveryService.updateDeliveryStatus(deliveryId, otp, userEmail);
    }
}
