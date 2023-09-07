package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Enums.OrderStatus;
import com.example.food.delivery.Request.OrderFilter;
import com.example.food.delivery.Request.OrderRequest;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<BaseResponse<?>> getAllOrders(int page);
    ResponseEntity<BaseResponse<?>> getOrdersByCustId(int page, String customerEmail);
    ResponseEntity<BaseResponse<?>> updatePaymentStatus(UpdatePaymentRequest updatePaymentRequest);
    ResponseEntity<BaseResponse<?>> getFilteredOrdersByCustId(int page, OrderFilter orderFilter);
    ResponseEntity<BaseResponse<?>> getFilteredOrders(int page, OrderStatus orderStatus);



}
