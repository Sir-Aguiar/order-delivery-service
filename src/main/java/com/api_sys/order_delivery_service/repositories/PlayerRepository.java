package com.api_sys.order_delivery_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_sys.order_delivery_service.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {

  Optional<Player> findByNickname(String nickname);

  boolean existsByNickname(String nickname);

  boolean existsByPlayerId(String playerId);
}
