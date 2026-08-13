package com.api_sys.order_delivery_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api_sys.order_delivery_service.entities.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

  @Query("SELECT i FROM Item i LEFT JOIN FETCH i.enchantments WHERE i.itemId = :itemId")
  Optional<Item> findByIdWithEnchantments(@Param("itemId") String itemId);
}
