package com.example.food.delivery;

import com.example.food.delivery.Request.OrderRequest;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/getOrder")
    public ResponseEntity<BaseResponse<?>> getAllOrders(@RequestParam(defaultValue = "0") int page){
        return orderService.getAllOrders(page);
    }

    @GetMapping(value = "/ordersByCustomer")
    public ResponseEntity<BaseResponse<?>> getOrdersByCustomer(@RequestParam(defaultValue = "0") int page, String customerEmail){
        return orderService.getOrdersByCustId(page, customerEmail);
    }

    @PutMapping("/updatePaymentStatus")
    public ResponseEntity<BaseResponse<?>> updatePaymentStatus(@Valid @RequestBody UpdatePaymentRequest updatePaymentRequest) {
        return orderService.updatePaymentStatus(updatePaymentRequest);
    }
}
