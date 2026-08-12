package com.api_sys.order_delivery_service.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_sys.order_delivery_service.dtos.CreateOrderRequest;
import com.api_sys.order_delivery_service.entities.Order;
import com.api_sys.order_delivery_service.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> create(@Valid @RequestBody CreateOrderRequest request) {
    Order created = orderService.create(request);
    URI location = URI.create("/orders/" + created.getOrderId());
    return ResponseEntity.created(location).body(created);
  }

  @GetMapping("/pending/{playerId}")
  public ResponseEntity<List<Order>> findPendingByPlayerId(@PathVariable String playerId) {
    return ResponseEntity.ok(orderService.findPendingByPlayerId(playerId));
  }

  @PatchMapping("/{orderId}/confirm")
  public ResponseEntity<Order> confirmDelivery(@PathVariable String orderId) {
    return ResponseEntity.ok(orderService.confirmDelivery(orderId));
  }
}
