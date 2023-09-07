package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Payment;
import com.example.food.delivery.Request.CartRequest;
import com.example.food.delivery.Request.PaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<BaseResponse<?>> addPayment(PaymentRequest paymentRequest);
    ResponseEntity<BaseResponse<?>> getAvailablePayments();
}
