package com.eecs4413.groupe.userservice.model;

import java.util.UUID;


import jakarta.persistence.*;

@Entity
@Table(
	    name = "wishlist",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"userId", "productId"}) /*To ensure same product is not added twice */
	    }
	)

public class WishListItem {
	 @Id
	 @GeneratedValue(strategy = GenerationType.UUID)
	 private UUID id;

	 @Column(nullable = false)
	 private UUID userId;

	 @Column(nullable = false)
	 private UUID productId;
	 
	 
	 public WishListItem() {}

	 public WishListItem(UUID userId, UUID productId) {
	       this.userId = userId;
	       this.productId = productId;
	 }

     public UUID getId() {
        return id;
     }

     public UUID getUserId() {
        return userId;
     }

     public UUID getProductId() {
        return productId;
     }

     public void setId(UUID id) {
        this.id = id;
     }

     public void setUserId(UUID userId) {
        this.userId = userId;
     }

     public void setProductId(UUID productId) {
        this.productId = productId;
     }

}
