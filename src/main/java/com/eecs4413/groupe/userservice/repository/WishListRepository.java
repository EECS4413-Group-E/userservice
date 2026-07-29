package com.eecs4413.groupe.userservice.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eecs4413.groupe.userservice.model.entity.WishListItem;

import java.util.List;
import java.util.UUID;

public interface WishListRepository extends JpaRepository<WishListItem, UUID> {

	@Query("SELECT w FROM WishListItem w WHERE w.userId = ?1")
	List<WishListItem> findByUserId(UUID userId);

	@Query("SELECT exists(SELECT w FROM WishListItem w WHERE w.userId = ?1 AND w.listingId = ?2)")
    boolean existsByUserIdAndlistingId(UUID userId, UUID listingId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM WishListItem w WHERE w.userId = ?1 AND w.listingId = ?2")
    void deleteByUserIdAndlistingId(UUID userId, UUID listingId);
}