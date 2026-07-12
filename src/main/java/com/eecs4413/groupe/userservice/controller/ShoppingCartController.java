package com.eecs4413.groupe.userservice.controller;

import com.eecs4413.groupe.userservice.model.request.AddShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.request.UpdateShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.service.ShoppingCartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartItemResponse>> getCart(@PathVariable UUID userId) {
        List<ShoppingCartItemResponse> cartItems =shoppingCartService.getCart(userId);

        return ResponseEntity.ok(cartItems);
    }

    @PostMapping
    public ResponseEntity<ShoppingCartItemResponse> addItem(
            @PathVariable UUID userId,
            @Valid
            @RequestBody
            AddShoppingCartItemRequest request
    ) {
        ShoppingCartItemResponse response =
                shoppingCartService.addItem(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{productId}/{size}")
    public ResponseEntity<ShoppingCartItemResponse> updateQuantity(
            @PathVariable UUID userId,
            @PathVariable UUID productId,
            @PathVariable String size,
            @Valid
            @RequestBody
            UpdateShoppingCartItemRequest request
    ) {
        ShoppingCartItemResponse response =
                shoppingCartService.updateQuantity(
                        userId,
                        productId,
                        size,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/{size}")
    public ResponseEntity<Void> removeItem(
            @PathVariable UUID userId,
            @PathVariable UUID productId,
            @PathVariable String size
    ) {
        shoppingCartService.removeItem(
                userId,
                productId,
                size
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @PathVariable UUID userId
    ) {
        shoppingCartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}