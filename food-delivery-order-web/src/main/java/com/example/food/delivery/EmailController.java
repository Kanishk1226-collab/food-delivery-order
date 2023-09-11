package com.example.food.delivery;

import com.example.food.delivery.Request.EmailRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@RestController
public class EmailController {
    private final EmailServiceImpl emailService;

    @Autowired
    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }
    @PostMapping("/send/otp")
    @PreAuthorize("#userRole == 'DELIVERY_AGENT'")
    public ResponseEntity<BaseResponse<?>> sendDeliveryOtp(@RequestParam int deliveryId,
                                                           @RequestHeader("userEmail") String userEmail,
                                                           @RequestHeader("userRole") String userRole) {
        return emailService.sendOtp(deliveryId, userEmail);
    }
}
