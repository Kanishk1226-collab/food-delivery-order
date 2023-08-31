package com.example.food.delivery;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, Integer> {
    boolean existsByCartId(String cartId);
    Cart findByCartId(String cartId);
}
