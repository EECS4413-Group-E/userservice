package com.eecs4413.groupe.userservice.repository;

import com.eecs4413.groupe.userservice.model.WishListItem;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WishListRepository extends JpaRepository<WishListItem, UUID> {

	@Transactional
	@Query("SELECT w FROM WishListItem w WHERE w.userId = ?1")
	List<WishListItem> findByUserId(UUID userId);
	
	@Transactional
	@Query("SELECT exists(SELECT w FROM WishListItem w WHERE w.userId = ?1 AND w.productId = ?2)")
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM WishListItem w WHERE w.userId = ?1 AND w.productId = ?2")
    void deleteByUserIdAndProductId(UUID userId, UUID productId);
}