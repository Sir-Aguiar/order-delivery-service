package com.api_sys.order_delivery_service.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api_sys.order_delivery_service.dtos.CreatePlayerRequest;
import com.api_sys.order_delivery_service.entities.Player;
import com.api_sys.order_delivery_service.repositories.PlayerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {

  private final PlayerRepository playerRepository;

  public Player create(CreatePlayerRequest request) {
    if (playerRepository.existsByNickname(request.getNickname())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um jogador com este nickname");
    }

    if (playerRepository.existsByPlayerId(request.getPlayerId())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um jogador com este playerId");
    }

    Player player = new Player();
    player.setNickname(request.getNickname());
    player.setPlayerId(request.getPlayerId());

    return playerRepository.save(player);
  }

  public List<Player> findAll() {
    return playerRepository.findAll();
  }

  public Player findByNickname(String nickname) {
    return playerRepository.findByNickname(nickname)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
  }

  public Player findByPlayerId(String playerId) {
    return playerRepository.findById(playerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
  }
}
