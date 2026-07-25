package com.eecs4413.groupe.userservice.controller;

import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.enums.Size;
import com.eecs4413.groupe.userservice.model.request.ShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.service.ShoppingCartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/cart")
@Validated
public class ShoppingCartController {

    private final ShoppingCartService _shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this._shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartItemResponse>> getCart(@PathVariable UUID userId) {
        List<ShoppingCartItemResponse> cartItems = _shoppingCartService.getCart(userId);

        return ResponseEntity.ok(cartItems);
    }

    @PostMapping
    public ResponseEntity<ShoppingCartItemResponse> addItem(
            @PathVariable UUID userId,
            @Valid @RequestBody ShoppingCartItemRequest request
    ) {
        ShoppingCartItemResponse response = _shoppingCartService.addItem(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping()
    public ResponseEntity<List<ShoppingCartItemResponse>> replaceUserCart(
            @PathVariable UUID userId,
            @Valid @RequestBody List<ShoppingCartItemRequest> request
    ) {
        List<ShoppingCartItem> results = _shoppingCartService.replaceUserCart(userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(results.stream().map(ShoppingCartItemResponse::from).toList());
    }

    @PatchMapping()
    public ResponseEntity<ShoppingCartItemResponse> updateQuantity(
            @PathVariable UUID userId,
            @Valid @RequestBody ShoppingCartItemRequest request
    ) {
        ShoppingCartItemResponse response = _shoppingCartService.updateQuantity(userId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/{size}")
    public ResponseEntity<Void> removeItem(
            @PathVariable UUID userId,
            @PathVariable UUID productId,
            @PathVariable Size size
    ) {
        _shoppingCartService.removeItem(
                userId,
                productId,
                size
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        _shoppingCartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}