package com.example.food.delivery;

import com.example.food.delivery.Request.CartRequest;
import com.example.food.delivery.Request.OrderRequest;
import com.example.food.delivery.Request.UpdateCartRequest;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.ServiceInterface.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping(value = "/create")
    public ResponseEntity<BaseResponse<?>> createCart(@Valid @RequestBody CartRequest cartRequest){
        return cartService.createCart(cartRequest);
    }

    @GetMapping(value = "/view")
    public ResponseEntity<BaseResponse<?>> getCart(@RequestParam String cartId){
        return cartService.getCart(cartId);
    }

    @PutMapping("/add")
    public ResponseEntity<BaseResponse<?>> updateCart(@Valid @RequestBody UpdateCartRequest updateCartRequest) {
        return cartService.updateCart(updateCartRequest);
    }

    @GetMapping("/checkout")
    @PreAuthorize("#userRole == 'CUSTOMER'")
    public ResponseEntity<BaseResponse<?>> checkoutCart(int addressId,
                                                        @RequestHeader("userEmail") String userEmail,
                                                        @RequestHeader("userRole") String userRole) {
        return cartService.checkoutCart(userEmail, addressId);
    }

    @PutMapping("/clear")
    public ResponseEntity<BaseResponse<?>> clearCart(@RequestParam String cartId) {
        return cartService.clearCart(cartId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<?>> deleteCustomer(@RequestParam String cartId) {
        return cartService.deleteCart(cartId);
    }
}
