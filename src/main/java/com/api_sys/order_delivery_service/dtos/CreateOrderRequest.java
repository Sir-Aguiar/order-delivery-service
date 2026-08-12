package com.api_sys.order_delivery_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @NotBlank(message = "O UUID do jogador é obrigatório")
  private String playerId;

  @NotBlank(message = "O UUID do item é obrigatório")
  private String itemId;
}
