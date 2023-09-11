package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.CartRequest;
import com.example.food.delivery.Request.UpdateCartRequest;
import com.example.food.delivery.Response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<BaseResponse<?>> createCart(CartRequest cartRequest);
    ResponseEntity<BaseResponse<?>> getCart(String cartId);
    ResponseEntity<BaseResponse<?>> checkoutCart(String cartId, int addressId);
    ResponseEntity<BaseResponse<?>> updateCart(UpdateCartRequest updateCartRequest);
    ResponseEntity<BaseResponse<?>> clearCart(String cartId);
    ResponseEntity<BaseResponse<?>> deleteCart(String cartId);

}
