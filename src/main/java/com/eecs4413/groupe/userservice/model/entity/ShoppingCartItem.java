package com.eecs4413.groupe.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

import com.eecs4413.groupe.userservice.model.enums.Size;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false
    )
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "size",
            nullable = false,
            updatable = false,
            length = 20
    )
    private Size size;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(
            User user,
            UUID productId,
            Size size,
            int quantity
    ) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}