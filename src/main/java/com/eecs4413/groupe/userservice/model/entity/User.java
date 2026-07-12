package com.eecs4413.groupe.userservice.model.entity;

import com.eecs4413.groupe.userservice.model.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;


import java.util.UUID;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false)
    @Email(message = "Invalid email format")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;
    
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }
    
    public void setShoppingCartItems(
            List<ShoppingCartItem> shoppingCartItems
    ) {
        this.shoppingCartItems.clear();

        if (shoppingCartItems != null) {
            for (ShoppingCartItem item : shoppingCartItems) {
                addShoppingCartItem(item);
            }
        }
    }
    
    public void addShoppingCartItem(
            ShoppingCartItem shoppingCartItem
    ) {
        shoppingCartItems.add(shoppingCartItem);
        shoppingCartItem.setUser(this);
    }
    
    public void removeShoppingCartItem(
            ShoppingCartItem shoppingCartItem
    ) {
        shoppingCartItems.remove(shoppingCartItem);
        shoppingCartItem.setUser(null);
    }
}
