package com.eecs4413.groupe.userservice.model.entity;

import java.util.UUID;


import jakarta.persistence.*;

@Entity
@Table(
	    name = "wishlist",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"userId", "listingId"}) /*To ensure same product is not added twice */
	    }
	)

public class WishListItem {
	 @Id
	 @GeneratedValue(strategy = GenerationType.UUID)
	 private UUID id;

	 @Column(nullable = false)
	 private UUID userId;

	 @Column(nullable = false)
	 private UUID listingId;
	 
	 
	 public WishListItem() {}

	 public WishListItem(UUID userId, UUID listingId) {
	       this.userId = userId;
	       this.listingId = listingId;
	 }

     public UUID getId() {
        return id;
     }

     public UUID getUserId() {
        return userId;
     }

     public UUID getListingId() {
        return listingId;
     }

     public void setId(UUID id) {
        this.id = id;
     }

     public void setUserId(UUID userId) {
        this.userId = userId;
     }

     public void setListingId(UUID listingId) {
        this.listingId = listingId;
     }

}
