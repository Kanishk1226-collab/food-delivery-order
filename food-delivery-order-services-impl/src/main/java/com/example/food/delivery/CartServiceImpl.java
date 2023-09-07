package com.example.food.delivery;

import com.example.food.delivery.Enums.OrderStatus;
import com.example.food.delivery.Request.*;
import com.example.food.delivery.Response.*;
import com.example.food.delivery.ServiceInterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public BaseResponse<?> response;

    public synchronized ResponseEntity<BaseResponse<?>> createCart(CartRequest cartRequest) {
        try {
            Cart cart = new Cart();
            if(cartRepository.existsByCartId(cartRequest.getCartId())){
                throw new OrderManagementExceptions.DuplicateException("Cart Id already exists");
            }
            cart.setId(sequenceGeneratorService.generateSequence(Cart.SEQUENCE_NAME));
            cart.setCartId(cartRequest.getCartId());
            cartRepository.save(cart);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Cart Created Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> getCartId(String cartId) {
        try{
            if(!cartRepository.existsByCartId(cartId)) {
                throw new OrderManagementExceptions.CartNotFound("Cart Id not found");
            }
            Cart cart = cartRepository.findByCartId(cartId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, cart);
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized ResponseEntity<BaseResponse<?>> checkoutCart(String cartId, int addressId) {
        try {
            isValidEmail(cartId);
            if(!cartRepository.existsByCartId(cartId)) {
                throw new OrderManagementExceptions.CartNotFound("Cart Id not found");
            }
            Cart cart = cartRepository.findByCartId(cartId);
            if(cart.getOrderItem() == null){
                throw new OrderManagementExceptions.OrderItemNotFound("Cart is Empty");
            }
            checkMenuAvailability(cart.getOrderItem());
            CustomerAddressResponse custAddress = getCustomerDetail(cartId, addressId);
            RestaurantResponse restResponse = getRestaurantDetail(cart.getRestaurantId());
            DeliveryAgentResponse delAgent = getDeliveryAgentDetail(restResponse.getRestAgentEmail());
            createOrder(cart.getOrderItem(), custAddress, delAgent, restResponse, cart.getTotalAmount());
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Order created successfully. Proceed to payment Page");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public void checkMenuAvailability(List<OrderItem> orderItemRes) {
        for (OrderItem orderItem : orderItemRes) {
            String url = "http://localhost:8082/restaurant-service/menuItem/getMenuDetail?menuItemId=" + orderItem.getMenuItemId() + "&quantity=" + orderItem.getQuantity();
            BaseResponse<CartResponse> getMenuDetail;
            ResponseEntity<BaseResponse<CartResponse>> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<BaseResponse<CartResponse>>() {});
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                getMenuDetail = responseEntity.getBody();
                if (!getMenuDetail.isSuccess()) {
                    throw new OrderManagementExceptions.RestTemplateException(getMenuDetail.getError());
                }
            }
        }
    }

    public CustomerAddressResponse getCustomerDetail(String cartId, int addressId) {
        String custAddressUrl = "http://localhost:8081/user-service/customerAddress/addressDetail?customerEmail=" + cartId + "&addressId=" + addressId;
        BaseResponse<CustomerAddressResponse> getCustomerAddress;
        ResponseEntity<BaseResponse<CustomerAddressResponse>> responseEntity1 =
                restTemplate.exchange(custAddressUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<BaseResponse<CustomerAddressResponse>>() {});
        if (responseEntity1.getStatusCode().is2xxSuccessful()) {
            getCustomerAddress = responseEntity1.getBody();
            if (!getCustomerAddress.isSuccess()) {
                throw new OrderManagementExceptions.RestTemplateException(getCustomerAddress.getError());
            }
            return getCustomerAddress.getData();
        }
        return null;
    }

    public RestaurantResponse getRestaurantDetail(Integer restaurantId) {
        String restUrl = "http://localhost:8082/restaurant-service/restaurant/getRestById?restId=" + restaurantId;
        BaseResponse<RestaurantResponse> getRestResponse;
        ResponseEntity<BaseResponse<RestaurantResponse>> responseEntity2 =
                restTemplate.exchange(restUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<BaseResponse<RestaurantResponse>>() {});
        if (responseEntity2.getStatusCode().is2xxSuccessful()) {
            getRestResponse = responseEntity2.getBody();
            if (!getRestResponse.isSuccess()) {
                throw new OrderManagementExceptions.RestTemplateException(getRestResponse.getError());
            }
            return getRestResponse.getData();
        }
        return null;
    }

    public DeliveryAgentResponse getDeliveryAgentDetail(String restAgentEmail) {
        String delAgentUrl = "http://localhost:8081/user-service/deliveryAgent/assignDelivery?restaurantAgentEmail=" + restAgentEmail;
        BaseResponse<DeliveryAgentResponse> getDelAgent;

        ResponseEntity<BaseResponse<DeliveryAgentResponse>> responseEntity3 =
                restTemplate.exchange(delAgentUrl, HttpMethod.PUT, null,
                        new ParameterizedTypeReference<BaseResponse<DeliveryAgentResponse>>() {});
        if (responseEntity3.getStatusCode().is2xxSuccessful()) {
            getDelAgent = responseEntity3.getBody();
            if (!getDelAgent.isSuccess()) {
                throw new OrderManagementExceptions.RestTemplateException(getDelAgent.getError());
            }
            return getDelAgent.getData();
        }
        return null;
    }

    public void createOrder(List<OrderItem> orderItemRes, CustomerAddressResponse custDetail, DeliveryAgentResponse delAgentDetail, RestaurantResponse restDetail, double totalPrice) {
        List<OrderItem> orderItems = orderItemRes;

        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setCustomerEmail(custDetail.getCustEmail());
        customerDetail.setCustomerName(custDetail.getCustName());
        customerDetail.setCustomerPhone(custDetail.getCustPhone());
        customerDetail.setDoorNo(custDetail.getDoorNo());
        customerDetail.setCity(custDetail.getCity());
        customerDetail.setLocality(custDetail.getLocality());
        customerDetail.setPincode(custDetail.getPincode());

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentMethod("Credit/Debit");
        paymentDetail.setIsPaid(true);
        paymentDetail.setIsOfflinePayment(false);

        RestaurantDetail restaurantDetail = new RestaurantDetail();
        restaurantDetail.setRestName(restDetail.getRestName());
        restaurantDetail.setRestAgentEmail(restDetail.getRestAgentEmail());
        restaurantDetail.setAvgRating(restDetail.getAvgRating());
        restaurantDetail.setLocation(restDetail.getLocation());
        restaurantDetail.setVeg(restDetail.isVeg());

        DeliveryAgentDetail deliveryAgentDetail = DeliveryAgentDetail.builder()
                .delAgentName(delAgentDetail.getDelAgentName())
                .delAgentEmail(delAgentDetail.getDelAgentEmail())
                .delAgentPhone(delAgentDetail.getDelAgentPhone())
                .build();

        Order order = new Order();
        order.setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));
        order.setCartId(custDetail.getCustEmail());
        order.setRestaurantName(restDetail.getRestName());
        order.setTotalAmount(totalPrice);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderItem(orderItems);
        order.setCustomerDetail(customerDetail);
        order.setPaymentDetail(paymentDetail);
        order.setRestaurantDetail(restaurantDetail);
        order.setDeliveryAgentDetail(deliveryAgentDetail);
        Order newOrder = orderRepository.save(order);

        DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                .deliveryPersonId(delAgentDetail.getDelAgentEmail())
                .orderId(newOrder.getId())
                .isDelivered(false).build();
        createDelivery(deliveryRequest);
        clearCartById(custDetail.getCustEmail());
        System.out.println("Order created successfully");
    }

    public synchronized void createDelivery(DeliveryRequest deliveryRequest) {
            Delivery delivery = new Delivery();
            delivery.setId(sequenceGeneratorService.generateSequence(Delivery.SEQUENCE_NAME));
            delivery.setOrderId(deliveryRequest.getOrderId());
            delivery.setDeliveryPersonId(deliveryRequest.getDeliveryPersonId());
            delivery.setIsDelivered(false);
            deliveryRepository.save(delivery);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Delivery Created Successfully");
    }

    public synchronized ResponseEntity<BaseResponse<?>> updateCart(UpdateCartRequest updateCartRequest) {
        try {
            if(!cartRepository.existsByCartId(updateCartRequest.getCartId())) {
                throw new OrderManagementExceptions.CartNotFound("Cart Id not found");
            }
            int menuItemId = updateCartRequest.getMenuItemId();
            int quantity = updateCartRequest.getQuantity();
            String url = "http://localhost:8082/restaurant-service/menuItem/getMenuDetail?menuItemId=" + menuItemId + "&quantity=" + quantity;
            BaseResponse<CartResponse> getMenuDetail;
            CartResponse cartResponse = new CartResponse();
            ResponseEntity<BaseResponse<CartResponse>> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<BaseResponse<CartResponse>>() {});
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                 getMenuDetail = responseEntity.getBody();
                if (!getMenuDetail.isSuccess()) {
                    throw new OrderManagementExceptions.RestTemplateException(getMenuDetail.getError());
                }
                cartResponse = getMenuDetail.getData();
            }
            Cart cart = cartRepository.findByCartId(updateCartRequest.getCartId());
            if(!(cart.getOrderItem() == null)) {
                if(!Objects.equals(cart.getRestaurantId(), cartResponse.getRestaurantId())) {
                    throw new OrderManagementExceptions.DiffRestaurantIdException("You cannot purchase items in different restaurants");
                }
            }
            List<OrderItem> orderItems = cart.getOrderItem();
            if(cart.getOrderItem() == null){
                orderItems = new ArrayList<>();
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuType(cartResponse.getMenuTypeName());
                orderItem.setMenuItemId(menuItemId);
                orderItem.setMenuItem(cartResponse.getMenuItemName());
                orderItem.setQuantity(quantity);
                orderItem.setPrice(cartResponse.getTotalPrice());
                orderItems.add(orderItem);
            } else if(containsMenuItemId(orderItems, menuItemId) != -1){
                orderItems = cart.getOrderItem();
                int index = (containsMenuItemId(orderItems, menuItemId));
                int existQuantity = orderItems.get(index).getQuantity();
                double existPrice = orderItems.get(index).getPrice();
                orderItems.get(index).setQuantity(existQuantity + quantity);
                orderItems.get(index).setPrice(existPrice + cartResponse.getTotalPrice());
            } else {
                orderItems = cart.getOrderItem();
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuType(cartResponse.getMenuTypeName());
                orderItem.setMenuItemId(menuItemId);
                orderItem.setMenuItem(cartResponse.getMenuItemName());
                orderItem.setQuantity(quantity);
                orderItem.setPrice(cartResponse.getTotalPrice());
                orderItems.add(orderItem);
            }
            cart.setOrderItem(orderItems);
            cart.setRestaurantId(cartResponse.getRestaurantId());
            cart.setRestaurantName(cartResponse.getRestaurantName());
            cart.setTotalAmount(calculateTotalPrice(orderItems));
            cartRepository.save(cart);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Cart updated Successfully");
        } catch(Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public int containsMenuItemId(List<OrderItem> orderItems, Integer menuItemIdToCheck) {
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            if (orderItem.getMenuItemId().equals(menuItemIdToCheck)) {
                return i;
            }
        }
        return -1;
    }

    public double calculateTotalPrice(List<OrderItem> orderItems) {
        double totalPrice = 0.0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice();
        }
        return totalPrice;
    }

    public synchronized ResponseEntity<BaseResponse<?>> clearCart(String cartId) {
        try{
            if(!cartRepository.existsByCartId(cartId)) {
                throw new OrderManagementExceptions.CartNotFound("Cart Id not found");
            }
            clearCartById(cartId);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Cart cleared successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public synchronized void clearCartById(String cartId) {
        Cart cart = cartRepository.findByCartId(cartId);
        cart.setTotalAmount(0.0);
        cart.setOrderItem(null);
        cart.setRestaurantId(0);
        cart.setRestaurantName("");
        cartRepository.save(cart);
    }

    public synchronized ResponseEntity<BaseResponse<?>> deleteCart(String cartId) {
        try {
            isValidEmail(cartId);
            Cart cart = cartRepository.findByCartId(cartId);
            if(cart == null) {
                throw new OrderManagementExceptions.CartNotFound("Cart not found");
            }
            cartRepository.deleteById((int) cart.getId());
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "Cart deleted Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

    public void isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            throw new OrderManagementExceptions.CartNotFound("Cart Id not found");
        }
    }
}
