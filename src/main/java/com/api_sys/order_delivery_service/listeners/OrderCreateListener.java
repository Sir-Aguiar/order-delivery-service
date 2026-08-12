package com.api_sys.order_delivery_service.listeners;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.api_sys.order_delivery_service.config.RabbitMQConfig;
import com.api_sys.order_delivery_service.dtos.CreateOrderRequest;
import com.api_sys.order_delivery_service.entities.Order;
import com.api_sys.order_delivery_service.services.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateListener {

  private final OrderService orderService;

  @RabbitListener(queues = RabbitMQConfig.ORDERS_CREATE_QUEUE)
  public void onOrderCreate(CreateOrderRequest request) {
    log.info("Pedido recebido via RabbitMQ: playerId={}, itemId={}", request.getPlayerId(), request.getItemId());

    try {
      Order order = orderService.create(request);
      log.info("Pedido criado via RabbitMQ: orderId={}", order.getOrderId());
    } catch (ResponseStatusException ex) {
      log.warn("Falha ao criar pedido via RabbitMQ: {}", ex.getReason());
      throw new AmqpRejectAndDontRequeueException(ex.getReason(), ex);
    }
  }
}
