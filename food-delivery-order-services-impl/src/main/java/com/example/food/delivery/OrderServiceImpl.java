package com.example.food.delivery;

import com.example.food.delivery.Request.OrderItemRequest;
import com.example.food.delivery.Request.OrderRequest;
import com.example.food.delivery.Request.UpdatePaymentRequest;
import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.ServiceInterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.food.delivery.Enums.OrderStatus.PENDING;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public BaseResponse<?> response;

    @Autowired
    private RestTemplate restTemplate;

    public synchronized ResponseEntity<BaseResponse<?>> getAllOrders(int page) {
        int pageSize = 10;
        Sort sortByOrderDateDesc = Sort.by(Sort.Direction.DESC, "order_date");
        PageRequest pageRequest = PageRequest.of(page, pageSize, sortByOrderDateDesc);
        Page<Order> order = orderRepository.findAll(pageRequest);
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, order.getContent());
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getOrdersByCustId(int page, String customerEmail) {
            try {
                isValidEmail(customerEmail);
                int pageSize = 10;
                PageRequest pageRequest = PageRequest.of(page, pageSize);
                Page<Order> pageOrder = orderRepository.findByCartIdOrderByIdDesc(customerEmail, pageRequest);
                List<Order> orders = pageOrder.getContent();
                response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, orders);
            } catch (Exception e) {
                response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
            }
            return ResponseEntity.ok(response);
        }

    public synchronized ResponseEntity<BaseResponse<?>> updatePaymentStatus(UpdatePaymentRequest updatePaymentRequest) {
        try {
            String cartId = updatePaymentRequest.getCartId();
            Boolean isPaid = updatePaymentRequest.getIsPaid();
            List<Order> order = orderRepository.findByCartIdAndOrderStatus(cartId, PENDING);
            if(order.isEmpty() || order == null) {
                throw new OrderManagementExceptions.OrderNotFound("No Orders Found");
            }
            order.get(0).getPaymentDetail().setIsPaid(isPaid);
            orderRepository.saveAll(order);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Payment Successful");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

        public void isValidEmail(String email) {
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if(!matcher.matches()) {
                throw new OrderManagementExceptions.InvalidInputException("Enter valid Customer Email");
            }
            String url = "http://localhost:8081/user-service/customer/isCustomerLoggedIn?customerEmail=" + email;
            BaseResponse<?> getCustomerAvail;
            ResponseEntity<BaseResponse<?>> responseEntity3 =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<BaseResponse<?>>() {});
            if (responseEntity3.getStatusCode().is2xxSuccessful()) {
                getCustomerAvail = responseEntity3.getBody();
                if (!getCustomerAvail.isSuccess()) {
                    throw new OrderManagementExceptions.RestTemplateException(getCustomerAvail.getError());
                }
            }
        }

}
