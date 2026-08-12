package com.api_sys.order_delivery_service.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_sys.order_delivery_service.dtos.CreatePlayerRequest;
import com.api_sys.order_delivery_service.entities.Player;
import com.api_sys.order_delivery_service.services.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
@Validated
public class PlayerController {

  private final PlayerService playerService;

  @PostMapping
  public ResponseEntity<Player> create(@Valid @RequestBody CreatePlayerRequest request) {
    Player created = playerService.create(request);
    URI location = URI.create("/players/" + created.getNickname());
    return ResponseEntity.created(location).body(created);
  }

  @GetMapping
  public ResponseEntity<List<Player>> findAll() {
    return ResponseEntity.ok(playerService.findAll());
  }

  @GetMapping("/{nickname}")
  public ResponseEntity<Player> findByNickname(@PathVariable String nickname) {
    return ResponseEntity.ok(playerService.findByNickname(nickname));
  }
}
