package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.InvalidShoppingCartQuantityException;
import com.eecs4413.groupe.userservice.exception.ShoppingCartItemNotFoundException;
import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.entity.User;
import com.eecs4413.groupe.userservice.model.enums.Size;
import com.eecs4413.groupe.userservice.model.request.ShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.repository.ShoppingCartItemRepository;
import com.eecs4413.groupe.userservice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShoppingCartService {

    private final ShoppingCartItemRepository _shoppingCartItemRepository;
    private final UserRepository _userRepository;

    public ShoppingCartService(
            ShoppingCartItemRepository shoppingCartItemRepository,
            UserRepository userRepository
    ) {
        this._shoppingCartItemRepository =
                shoppingCartItemRepository;
        this._userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ShoppingCartItemResponse> getCart(UUID userId) {
        validateUserExists(userId);

        return _shoppingCartItemRepository
                .findAllByUserId(userId)
                .stream()
                .map(ShoppingCartItemResponse::from)
                .toList();
    }

    @Transactional
    public ShoppingCartItemResponse addItem(UUID userId, ShoppingCartItemRequest request) {
        User user = _userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        validateQuantity(request.quantity());

        ShoppingCartItem cartItem = _shoppingCartItemRepository.findByUserIdAndProductIdAndSize(
                        userId,
                        request.productId(),
                        request.size()
                )
                .map(existingItem -> {
                    int newQuantity;

                    try {
                        newQuantity = Math.addExact(
                                existingItem.getQuantity(),
                                request.quantity()
                        );
                    } catch (ArithmeticException exception) {
                        throw new InvalidShoppingCartQuantityException(
                                Integer.MAX_VALUE
                        );
                    }

                    validateQuantity(newQuantity);
                    existingItem.setQuantity(newQuantity);

                    return existingItem;
                })
                .orElseGet(() -> new ShoppingCartItem(
                        user,
                        request.productId(),
                        request.size(),
                        request.quantity())
                );

        ShoppingCartItem savedItem = _shoppingCartItemRepository.save(cartItem);

        return ShoppingCartItemResponse.from(savedItem);
    }

    @Transactional
    public List<ShoppingCartItem> replaceUserCart(UUID userId, List<ShoppingCartItemRequest> request) {
        User user = _userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        _shoppingCartItemRepository.deleteAllByUserId(userId);
        _shoppingCartItemRepository.flush();

        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

        for(ShoppingCartItemRequest itemRequest : request) {
            validateQuantity(itemRequest.quantity());

            shoppingCartItems.add(new ShoppingCartItem(
                    user,
                    itemRequest.productId(),
                    itemRequest.size(),
                    itemRequest.quantity()));
        }

        return _shoppingCartItemRepository.saveAllAndFlush(shoppingCartItems);
    }

    @Transactional
    public ShoppingCartItemResponse updateQuantity(UUID userId, ShoppingCartItemRequest request) {
        validateUserExists(userId);
        validateQuantity(request.quantity());

        ShoppingCartItem cartItem =
                _shoppingCartItemRepository
                        .findByUserIdAndProductIdAndSize(
                                userId,
                                request.productId(),
                                request.size()
                        )
                        .orElseThrow(() ->
                                new ShoppingCartItemNotFoundException(
                                        userId,
                                        request.productId(),
                                        request.size()
                                )
                        );

        cartItem.setQuantity(request.quantity());

        ShoppingCartItem updatedItem = _shoppingCartItemRepository.save(cartItem);

        return ShoppingCartItemResponse.from(updatedItem);
    }

    @Transactional
    public void removeItem(UUID userId, UUID productId, Size size) {
        validateUserExists(userId);

        long deletedItems = _shoppingCartItemRepository.deleteByUserIdAndProductIdAndSize(
                userId,
                productId,
                size
        );

        if (deletedItems == 0) {
            throw new ShoppingCartItemNotFoundException(
                    userId,
                    productId,
                    size
            );
        }
    }

    @Transactional
    public void clearCart(UUID userId) {
        validateUserExists(userId);

        _shoppingCartItemRepository.deleteAllByUserId(userId);
    }

    private void validateUserExists(UUID userId) {
        if (!_userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new InvalidShoppingCartQuantityException(quantity);
        }
    }
}