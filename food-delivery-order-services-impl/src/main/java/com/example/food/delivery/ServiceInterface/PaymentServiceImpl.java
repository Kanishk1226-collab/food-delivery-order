package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.CartRepository;
import com.example.food.delivery.PaymentRepository;
import com.example.food.delivery.Request.PaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public BaseResponse<?> response;

    @Override
    public ResponseEntity<BaseResponse<?>> addPayment(PaymentRequest paymentRequest) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse<?>> getAvailablePayments() {
        return null;
    }
}
