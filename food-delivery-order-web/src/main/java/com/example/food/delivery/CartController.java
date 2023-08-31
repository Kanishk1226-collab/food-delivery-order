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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping(value = "/createCart")
    public ResponseEntity<BaseResponse<?>> createCart(@Valid @RequestBody CartRequest cartRequest){
        return cartService.createCart(cartRequest);
    }

    @GetMapping(value = "/getCart")
    public ResponseEntity<BaseResponse<?>> getCart(@RequestParam String cartId){
        return cartService.getCartId(cartId);
    }

    @PutMapping("/updateCart")
    public ResponseEntity<BaseResponse<?>> updateCart(@Valid @RequestBody UpdateCartRequest updateCartRequest) {
        return cartService.updateCart(updateCartRequest);
    }

    @GetMapping("/checkoutCart")
    public ResponseEntity<BaseResponse<?>> checkoutCart(@RequestParam String cartId, int addressId) {
        return cartService.checkoutCart(cartId, addressId);
    }

    @PutMapping("/clearCart")
    public ResponseEntity<BaseResponse<?>> clearCart(@RequestParam String cartId) {
        return cartService.clearCart(cartId);
    }

    @DeleteMapping("/deleteCart")
    public ResponseEntity<BaseResponse<?>> deleteCustomer(@RequestParam String cartId) {
        return cartService.deleteCart(cartId);
    }
}
