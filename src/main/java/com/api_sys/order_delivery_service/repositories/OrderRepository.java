package com.api_sys.order_delivery_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api_sys.order_delivery_service.entities.Order;
import com.api_sys.order_delivery_service.entities.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, String> {

  @Query("""
      SELECT DISTINCT o FROM Order o
      JOIN FETCH o.player
      JOIN FETCH o.item i
      LEFT JOIN FETCH i.enchantments
      WHERE o.player.playerId = :playerId AND o.status = :status
      """)
  List<Order> findByPlayer_PlayerIdAndStatusWithItem(
      @Param("playerId") String playerId,
      @Param("status") OrderStatus status);

  boolean existsByPlayer_PlayerIdAndStatus(String playerId, OrderStatus status);

  @Query("""
      SELECT o FROM Order o
      JOIN FETCH o.item i
      LEFT JOIN FETCH i.enchantments
      WHERE o.orderId = :orderId
      """)
  Optional<Order> findByIdWithItem(@Param("orderId") String orderId);
}
