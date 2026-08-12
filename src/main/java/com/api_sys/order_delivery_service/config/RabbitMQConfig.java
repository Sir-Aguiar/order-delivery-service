package com.api_sys.order_delivery_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api_sys.order_delivery_service.dtos.CreateOrderRequest;

@Configuration
public class RabbitMQConfig {

  public static final String ORDERS_EXCHANGE = "orders.exchange";
  public static final String ORDERS_CREATE_QUEUE = "orders.create.queue";
  public static final String ORDERS_CREATE_ROUTING_KEY = "order.create";

  @Bean
  public DirectExchange ordersExchange() {
    return new DirectExchange(ORDERS_EXCHANGE);
  }

  @Bean
  public Queue ordersCreateQueue() {
    return QueueBuilder.durable(ORDERS_CREATE_QUEUE).build();
  }

  @Bean
  public Binding ordersCreateBinding(Queue ordersCreateQueue, DirectExchange ordersExchange) {
    return BindingBuilder.bind(ordersCreateQueue)
        .to(ordersExchange)
        .with(ORDERS_CREATE_ROUTING_KEY);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setDefaultType(CreateOrderRequest.class);
    converter.setClassMapper(classMapper);
    return converter;
  }
}
