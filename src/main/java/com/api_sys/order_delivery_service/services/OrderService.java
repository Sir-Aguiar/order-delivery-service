package com.api_sys.order_delivery_service.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api_sys.order_delivery_service.dtos.CreateOrderRequest;
import com.api_sys.order_delivery_service.entities.Item;
import com.api_sys.order_delivery_service.entities.Order;
import com.api_sys.order_delivery_service.entities.OrderStatus;
import com.api_sys.order_delivery_service.entities.Player;
import com.api_sys.order_delivery_service.repositories.ItemRepository;
import com.api_sys.order_delivery_service.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;
  private final PlayerService playerService;

  public Order create(CreateOrderRequest request) {
    Player player = playerService.findByPlayerId(request.getPlayerId());
    Item item = itemRepository.findByIdWithEnchantments(request.getItemId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Item não encontrado com itemId '%s'".formatted(request.getItemId())));

    Order order = new Order();
    order.setPlayer(player);
    order.setItem(item);
    order.setStatus(OrderStatus.PENDING);

    return orderRepository.save(order);
  }

  public List<Order> findPendingByPlayerId(String playerId) {
    playerService.findByPlayerId(playerId);
    return orderRepository.findByPlayer_PlayerIdAndStatusWithItem(playerId, OrderStatus.PENDING);
  }

  public boolean hasPendingByPlayerId(String playerId) {
    return orderRepository.existsByPlayer_PlayerIdAndStatus(playerId, OrderStatus.PENDING);
  }

  public Order confirmDelivery(String orderId) {
    Order order = orderRepository.findByIdWithItem(orderId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Pedido não encontrado com orderId '%s'".formatted(orderId)));

    if (order.getStatus() != OrderStatus.PENDING) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Somente pedidos PENDING podem ser confirmados");
    }

    order.setStatus(OrderStatus.DONE);
    return orderRepository.save(order);
  }
}
