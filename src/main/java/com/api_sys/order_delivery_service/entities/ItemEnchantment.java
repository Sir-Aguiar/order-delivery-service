package com.api_sys.order_delivery_service.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_enchantment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEnchantment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID itemEnchantmentId;

  @ManyToOne
  @JoinColumn(name = "item_id")
  @JsonIgnore
  private Item item;

  private Integer level;

  @Enumerated(EnumType.STRING)
  private Enchantment enchantment;
}
