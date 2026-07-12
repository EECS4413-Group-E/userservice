package com.eecs4413.groupe.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(
        name = "shopping_cart_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_user_product_size",
                        columnNames = {
                                "user_id",
                                "product_id",
                                "size"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_cart_user_id",
                        columnList = "user_id"
                )
        }
)
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    private UUID userId;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false
    )
    private UUID productId;

    @Column(
            name = "size",
            nullable = false,
            updatable = false,
            length = 20
    )
    private String size;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(
            UUID userId,
            UUID productId,
            String size,
            int quantity
    ) {
        this.userId = userId;
        this.productId = productId;
        this.size = size;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}