package com.api_sys.order_delivery_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  @Id
  @Column(name = "player_id", nullable = false, unique = true)
  private String playerId;

  @Column(name = "nickname", nullable = false, unique = true)
  private String nickname;
}
