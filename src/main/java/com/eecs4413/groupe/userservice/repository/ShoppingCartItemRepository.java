package com.eecs4413.groupe.userservice.repository;

import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.enums.Size;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, UUID> {

	List<ShoppingCartItem> findAllByUser_Id(UUID userId);

	Optional<ShoppingCartItem> findByUserIdAndProductIdAndSize(
	        UUID userId,
	        UUID productId,
	        Size size
	);

	long deleteByUserIdAndProductIdAndSize(
	        UUID userId,
	        UUID productId,
	        Size size
	);

	long deleteAllByUserId(UUID userId);
}