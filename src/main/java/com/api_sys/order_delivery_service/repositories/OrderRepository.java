package com.api_sys.order_delivery_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_sys.order_delivery_service.entities.Order;
import com.api_sys.order_delivery_service.entities.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, String> {

  List<Order> findByPlayer_PlayerIdAndStatus(String playerId, OrderStatus status);

  boolean existsByPlayer_PlayerIdAndStatus(String playerId, OrderStatus status);
}
