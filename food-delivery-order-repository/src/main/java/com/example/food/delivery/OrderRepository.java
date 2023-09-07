package com.example.food.delivery;

import com.example.food.delivery.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, Integer> {
    List<Order> findByCartIdAndOrderStatus(String cartId, OrderStatus orderStatus);

    Page<Order> findByCartIdOrderByIdDesc(String cartId, Pageable pageable);

    Order findById(long orderId);

    Page<Order> findByCartIdAndOrderStatusOrderByIdDesc(String cartId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByOrderStatusOrderByIdDesc(OrderStatus orderStatus, Pageable pageable);


}
