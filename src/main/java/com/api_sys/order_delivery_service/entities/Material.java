package com.api_sys.order_delivery_service.entities;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Materiais de ferramentas, seguindo a nomenclatura do enum Material do
 * Spigot (org.bukkit.Material).
 */
public enum Material {
  WOODEN_SWORD,
  STONE_SWORD,
  IRON_SWORD,
  GOLDEN_SWORD,
  DIAMOND_SWORD,
  NETHERITE_SWORD,

  WOODEN_PICKAXE,
  STONE_PICKAXE,
  IRON_PICKAXE,
  GOLDEN_PICKAXE,
  DIAMOND_PICKAXE,
  NETHERITE_PICKAXE,

  WOODEN_AXE,
  STONE_AXE,
  IRON_AXE,
  GOLDEN_AXE,
  DIAMOND_AXE,
  NETHERITE_AXE,

  WOODEN_SHOVEL,
  STONE_SHOVEL,
  IRON_SHOVEL,
  GOLDEN_SHOVEL,
  DIAMOND_SHOVEL,
  NETHERITE_SHOVEL,

  WOODEN_HOE,
  STONE_HOE,
  IRON_HOE,
  GOLDEN_HOE,
  DIAMOND_HOE,
  NETHERITE_HOE,

  BOW,
  CROSSBOW,
  TRIDENT,
  MACE,
  SHIELD,
  FISHING_ROD,
  FLINT_AND_STEEL,
  SHEARS,
  BRUSH;

  @JsonCreator
  public static Material fromValue(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    try {
      return Material.valueOf(value.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      String accepted = Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(", "));
      throw new IllegalArgumentException(
          "Material inválido: '%s'. Valores aceitos: %s".formatted(value, accepted));
    }
  }
}
