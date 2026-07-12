package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.InvalidShoppingCartQuantityException;
import com.eecs4413.groupe.userservice.exception.ShoppingCartItemNotFoundException;
import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.request.AddShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.request.UpdateShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.repository.ShoppingCartItemRepository;
import com.eecs4413.groupe.userservice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ShoppingCartService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;

    public ShoppingCartService(
            ShoppingCartItemRepository shoppingCartItemRepository,
            UserRepository userRepository
    ) {
        this.shoppingCartItemRepository =
                shoppingCartItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ShoppingCartItemResponse> getCart(UUID userId) {
        validateUserExists(userId);

        return shoppingCartItemRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ShoppingCartItemResponse addItem(
            UUID userId,
            AddShoppingCartItemRequest request
    ) {
        validateUserExists(userId);
        validateQuantity(request.quantity());

        String normalizedSize = normalizeSize(request.size());

        ShoppingCartItem cartItem =
                shoppingCartItemRepository
                        .findByUserIdAndProductIdAndSize(
                                userId,
                                request.productId(),
                                normalizedSize
                        )
                        .map(existingItem -> {
                            int newQuantity;

                            try {
                                newQuantity = Math.addExact(
                                        existingItem.getQuantity(),
                                        request.quantity()
                                );
                            } catch (ArithmeticException exception) {
                                throw new IllegalArgumentException(
                                        "Shopping cart quantity is too large"
                                );
                            }

                            validateQuantity(newQuantity);
                            existingItem.setQuantity(newQuantity);

                            return existingItem;
                        })
                        .orElseGet(() ->
                                new ShoppingCartItem(
                                        userId,
                                        request.productId(),
                                        normalizedSize,
                                        request.quantity()
                                )
                        );

        ShoppingCartItem savedItem =
                shoppingCartItemRepository.save(cartItem);

        return toResponse(savedItem);
    }

    @Transactional
    public ShoppingCartItemResponse updateQuantity(
            UUID userId,
            UUID productId,
            String size,
            UpdateShoppingCartItemRequest request
    ) {
        validateUserExists(userId);
        validateQuantity(request.quantity());

        String normalizedSize = normalizeSize(size);

        ShoppingCartItem cartItem =
                shoppingCartItemRepository
                        .findByUserIdAndProductIdAndSize(
                                userId,
                                productId,
                                normalizedSize
                        )
                        .orElseThrow(() ->
                                new ShoppingCartItemNotFoundException(
                                        userId,
                                        productId,
                                        normalizedSize
                                )
                        );

        cartItem.setQuantity(request.quantity());

        ShoppingCartItem updatedItem =
                shoppingCartItemRepository.save(cartItem);

        return toResponse(updatedItem);
    }

    @Transactional
    public void removeItem(
            UUID userId,
            UUID productId,
            String size
    ) {
        validateUserExists(userId);

        String normalizedSize = normalizeSize(size);

        long deletedItems =
                shoppingCartItemRepository
                        .deleteByUserIdAndProductIdAndSize(
                                userId,
                                productId,
                                normalizedSize
                        );

        if (deletedItems == 0) {
            throw new ShoppingCartItemNotFoundException(
                    userId,
                    productId,
                    normalizedSize
            );
        }
    }

    @Transactional
    public void clearCart(UUID userId) {
        validateUserExists(userId);

        shoppingCartItemRepository.deleteAllByUserId(userId);
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new InvalidShoppingCartQuantityException(
                    quantity
            );
        }
    }

    private String normalizeSize(String size) {
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException(
                    "Size is required"
            );
        }

        return size
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private ShoppingCartItemResponse toResponse(
            ShoppingCartItem cartItem
    ) {
        return new ShoppingCartItemResponse(
                cartItem.getId(),
                cartItem.getProductId(),
                cartItem.getSize(),
                cartItem.getQuantity()
        );
    }
}