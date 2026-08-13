package com.api_sys.order_delivery_service.entities;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Enchantment {
  SHARPNESS,
  SMITE,
  BANE_OF_ARTHROPODS,
  KNOCKBACK,
  FIRE_ASPECT,
  LOOTING,
  SWEEPING_EDGE,
  UNBREAKING,
  MENDING;

  @JsonCreator
  public static Enchantment fromValue(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    try {
      return Enchantment.valueOf(value.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      String accepted = Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(", "));
      throw new IllegalArgumentException(
          "Encantamento inválido: '%s'. Valores aceitos: %s".formatted(value, accepted));
    }
  }
}
