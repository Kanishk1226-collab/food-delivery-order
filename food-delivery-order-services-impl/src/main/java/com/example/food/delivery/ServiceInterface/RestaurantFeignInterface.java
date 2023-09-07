package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Request.RatingRequest;
import com.example.food.delivery.Response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "restaurant", url = "http://localhost:8082/restaurant-service/")
public interface RestaurantFeignInterface {
    @PostMapping(value = "createRating")
     ResponseEntity<BaseResponse<?>> createRating(@Valid @RequestBody RatingRequest ratingRequest);
}
