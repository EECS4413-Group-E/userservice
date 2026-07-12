package com.eecs4413.groupe.userservice.controller;

import com.eecs4413.groupe.userservice.model.enums.Size;
import com.eecs4413.groupe.userservice.model.request.AddShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.service.ShoppingCartService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/cart")
@Validated
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

    @PatchMapping("/{productId}/{size}")
    public ResponseEntity<ShoppingCartItemResponse> updateQuantity(
            @PathVariable UUID userId,
            @PathVariable UUID productId,
            @PathVariable Size size,
            @RequestParam
            @Min(
                    value = 1,
                    message = "Quantity must be at least 1"
            )
            int quantity) {
        ShoppingCartItemResponse response =
                shoppingCartService.updateQuantity(
                        userId,
                        productId,
                        size,
                        quantity
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/{size}")
    public ResponseEntity<Void> removeItem(
            @PathVariable UUID userId,
            @PathVariable UUID productId,
            @PathVariable Size size
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