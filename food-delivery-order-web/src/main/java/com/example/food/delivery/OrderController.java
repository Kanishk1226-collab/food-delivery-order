package com.example.food.delivery;

import com.example.food.delivery.Enums.OrderStatus;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/all")
    @PreAuthorize("#userRole == 'ADMIN' OR #userRole == 'CO_ADMIN' OR #userRole == 'SUPER_ADMIN'")
    public ResponseEntity<BaseResponse<?>> getAllOrders(@RequestParam(defaultValue = "0") int page){
        return orderService.getAllOrders(page);
    }

    @GetMapping(value = "/customer")
    @PreAuthorize("#userRole == 'CUSTOMER'")
    public ResponseEntity<BaseResponse<?>> getOrdersByCustomer(@RequestParam(defaultValue = "0") int page,
                                                               @RequestHeader("userEmail") String userEmail,
                                                               @RequestHeader("userRole") String userRole){
        return orderService.getOrdersByCustId(page, userEmail);
    }

    @GetMapping(value = "/filter")
    @PreAuthorize("#userRole == 'CUSTOMER'")
    public ResponseEntity<BaseResponse<?>> getFilteredOrdersByCustId(@RequestParam(defaultValue = "0") int page, OrderStatus orderStatus,
                                                                     @RequestHeader("userEmail") String userEmail,
                                                                     @RequestHeader("userRole") String userRole){
        return orderService.getFilteredOrdersByCustId(page, orderStatus, userEmail);
    }

    @GetMapping(value = "/all/filter")
    @PreAuthorize("#userRole == 'ADMIN' OR #userRole == 'CO_ADMIN' OR #userRole == 'SUPER_ADMIN'")
    public ResponseEntity<BaseResponse<?>> getFilteredOrders(@RequestParam(defaultValue = "0") int page, OrderStatus orderStatus,
                                                             @RequestHeader("userEmail") String userEmail,
                                                             @RequestHeader("userRole") String userRole){
        return orderService.getFilteredOrders(page, orderStatus);
    }

    @PutMapping("/updatePaymentStatus")
    public ResponseEntity<BaseResponse<?>> updatePaymentStatus(@Valid @RequestBody UpdatePaymentRequest updatePaymentRequest) {
        return orderService.updatePaymentStatus(updatePaymentRequest);
    }
}
