package com.example.food.delivery;

import com.example.food.delivery.Enums.OrderStatus;
import com.example.food.delivery.Request.*;
import com.example.food.delivery.Response.*;
import com.example.food.delivery.ServiceInterface.DeliveryService;
import com.example.food.delivery.ServiceInterface.RestaurantFeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.food.delivery.Enums.OrderStatus.PENDING;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RestaurantFeignInterface restaurantInterface;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    public BaseResponse<?> response;

    @Autowired
    private RestTemplate restTemplate;

    public synchronized ResponseEntity<BaseResponse<?>> createDelivery(DeliveryRequest deliveryRequest) {
        try {
            Delivery delivery = new Delivery();
            delivery.setId(sequenceGeneratorService.generateSequence(Delivery.SEQUENCE_NAME));
            delivery.setOrderId(deliveryRequest.getOrderId());
            delivery.setDeliveryPersonId(deliveryRequest.getDeliveryPersonId());
            delivery.setIsDelivered(false);
            deliveryRepository.save(delivery);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Delivery Created Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getAllDelivery(int page) {
        int pageSize = 10;
        Sort sortByDeliveryId = Sort.by(Sort.Direction.DESC, "delivery_id");
        PageRequest pageRequest = PageRequest.of(page, pageSize, sortByDeliveryId);
        Page<Delivery> delivery = deliveryRepository.findAll(pageRequest);
        response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, delivery.getContent());
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getDeliveryByDelAgent(String delAgentEmail, int page) {
        try {
            isValidEmail(delAgentEmail);
            int pageSize = 10;
            PageRequest pageRequest = PageRequest.of(page, pageSize);
            Page<Delivery> pageDelivery = deliveryRepository.findByDeliveryPersonIdOrderByIdDesc(delAgentEmail, pageRequest);
            List<Delivery> deliveryList = pageDelivery.getContent();
            List<DeliveryOrderResponse> deliveryOrder = new ArrayList<>();

            for (Delivery delivery: deliveryList) {
                Order order = orderRepository.findById(delivery.getOrderId());
                DeliveryOrderResponse delResponse = new DeliveryOrderResponse();
                delResponse.setOrderId(order.getId());
                delResponse.setIsDelivered(delivery.getIsDelivered());
                delResponse.setOrderDate(order.getOrderDate());
                delResponse.setTotalAmount(order.getTotalAmount());
                delResponse.setOrderStatus(order.getOrderStatus());
                List<OrderItemResponse> orderItems = new ArrayList<>();
                for (OrderItem item: order.getOrderItem()) {
                    OrderItemResponse orderItemResp = new OrderItemResponse();
                    orderItemResp.setMenuType(item.getMenuType());
                    orderItemResp.setMenuItemId(item.getMenuItemId());
                    orderItemResp.setMenuItem(item.getMenuItem());
                    orderItemResp.setQuantity(item.getQuantity());
                    orderItemResp.setPrice(item.getPrice());
                    orderItems.add(orderItemResp);
                }
                delResponse.setOrderItem(orderItems);
                CustomerDetailResponse custResp = CustomerDetailResponse.builder()
                        .custName(order.getCustomerDetail().getCustomerName())
                        .custEmail(order.getCustomerDetail().getCustomerEmail())
                        .custPhone(order.getCustomerDetail().getCustomerPhone())
                        .doorNo(order.getCustomerDetail().getDoorNo())
                        .locality(order.getCustomerDetail().getLocality())
                        .city(order.getCustomerDetail().getCity())
                        .pincode(order.getCustomerDetail().getPincode()).build();
                delResponse.setCustomerDetailResponse(custResp);
                deliveryOrder.add(delResponse);
            }
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, deliveryOrder);
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> updateDeliveryStatus(UpdateDeliveryRequest updateDeliveryRequest) {
        try {
            Optional<Delivery> optionalDelivery = deliveryRepository.findById(updateDeliveryRequest.getDeliveryId());
            if(optionalDelivery.isEmpty()) {
                throw new OrderManagementExceptions.DeliveryNotFound("Delivery with Id " + updateDeliveryRequest.getDeliveryId() + " not found");
            }
            Delivery delivery = optionalDelivery.get();
            if(!Objects.equals(delivery.getDeliveryPersonId(), updateDeliveryRequest.getDeliveryPersonId())) {
                throw new OrderManagementExceptions.DeliveryNotFound("Delivery with this user Id not found");
            }
            delivery.setIsDelivered(true);
            deliveryRepository.save(delivery);
            Order order = orderRepository.findById(delivery.getOrderId());
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            setRestAndDelAgentAvailability(delivery.getDeliveryPersonId());
            for (OrderItem orderItem : order.getOrderItem()) {
                createRating((int)delivery.getOrderId(), orderItem.getMenuItemId(), order.getCartId());
            }
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Item Delivered Successfully");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public void createRating(int orderId, int menuItemId, String customerEmail) {
        RatingRequest ratingRequest = RatingRequest.builder()
                .orderId(orderId)
                .menuItemId(menuItemId)
                .customerEmail(customerEmail)
                .build();
        restaurantInterface.createRating(ratingRequest);
    }

    public void setRestAndDelAgentAvailability(String delAgentEmail) {
        String url = "http://localhost:8081/user-service/deliveryAgent/setDelAgentAvailability?delAgentEmail=" + delAgentEmail;
        BaseResponse<?> getDelAgentAvail;
        ResponseEntity<BaseResponse<?>> responseEntity =
                restTemplate.exchange(url, HttpMethod.PUT, null,
                        new ParameterizedTypeReference<BaseResponse<?>>() {});
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            getDelAgentAvail = responseEntity.getBody();
            if (!getDelAgentAvail.isSuccess()) {
                throw new OrderManagementExceptions.RestTemplateException(getDelAgentAvail.getError());
            }
        }
    }

    public void isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new OrderManagementExceptions.InvalidInputException("Enter valid Delivery Person Email");
        }
        String url = "http://localhost:8081/user-service/deliveryAgent/isDeliveryAgentLoggedIn?delAgentEmail=" + email;
        BaseResponse<?> getDelAgentAvail;
        ResponseEntity<BaseResponse<?>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<BaseResponse<?>>() {});
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            getDelAgentAvail = responseEntity.getBody();
            if (!getDelAgentAvail.isSuccess()) {
                throw new OrderManagementExceptions.RestTemplateException(getDelAgentAvail.getError());
            }
        }
    }

}
