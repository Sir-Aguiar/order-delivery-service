package com.api_sys.order_delivery_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerRequest {

  @NotBlank(message = "O nickname é obrigatório")
  private String nickname;

  @NotBlank(message = "O playerId é obrigatório")
  private String playerId;
}
