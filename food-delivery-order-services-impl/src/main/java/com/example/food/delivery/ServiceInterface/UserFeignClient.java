package com.example.food.delivery.ServiceInterface;

import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.CustomerAddressResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", url = "http://localhost:8081/user-service/")
public interface UserFeignClient {
    @GetMapping(value = "/address/detail")
    ResponseEntity<BaseResponse<?>> getAddressDetail(@RequestParam int addressId, @RequestHeader String userRole, @RequestHeader String userEmail) ;

    @PutMapping("/delAgent/update/status")
    ResponseEntity<BaseResponse<?>> setAvailability(@RequestParam String status,
                                                    @RequestHeader String userEmail,
                                                    @RequestHeader String userRole);
}

