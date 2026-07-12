package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.InvalidShoppingCartQuantityException;
import com.eecs4413.groupe.userservice.exception.ShoppingCartItemNotFoundException;
import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.entity.User;
import com.eecs4413.groupe.userservice.model.enums.Size;
import com.eecs4413.groupe.userservice.model.request.AddShoppingCartItemRequest;
import com.eecs4413.groupe.userservice.model.response.ShoppingCartItemResponse;
import com.eecs4413.groupe.userservice.repository.ShoppingCartItemRepository;
import com.eecs4413.groupe.userservice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .findAllByUser_Id(userId)
                .stream()
                .map(ShoppingCartItemResponse::from)
                .toList();    }

    @Transactional
    public ShoppingCartItemResponse addItem(
            UUID userId,
            AddShoppingCartItemRequest request
    ) {
    	User user = userRepository
    	        .findById(userId)
    	        .orElseThrow(() ->
    	                new UserNotFoundException(userId)
    	        );
        validateQuantity(request.quantity());


        ShoppingCartItem cartItem =
                shoppingCartItemRepository
                        .findByUser_IdAndProductIdAndSize(
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
                        .orElseGet(() ->
                                new ShoppingCartItem(
                                        user,
                                        request.productId(),
                                        request.size(),
                                        request.quantity()
                                )
                        );

        ShoppingCartItem savedItem =
                shoppingCartItemRepository.save(cartItem);

        return  ShoppingCartItemResponse.from(savedItem);    }

    @Transactional
    public ShoppingCartItemResponse updateQuantity(
            UUID userId,
            UUID productId,
            Size size,
            int quantity    ) {
        validateUserExists(userId);
        validateQuantity(quantity);

        ShoppingCartItem cartItem =
                shoppingCartItemRepository
                        .findByUser_IdAndProductIdAndSize(
                                userId,
                                productId,
                                size
                        )
                        .orElseThrow(() ->
                                new ShoppingCartItemNotFoundException(
                                        userId,
                                        productId,
                                        size
                                )
                        );

        cartItem.setQuantity(quantity);

        ShoppingCartItem updatedItem =
                shoppingCartItemRepository.save(cartItem);

        return ShoppingCartItemResponse.from(updatedItem);
    }

    @Transactional
    public void removeItem(
            UUID userId,
            UUID productId,
            Size size
    ) {
        validateUserExists(userId);


        long deletedItems =
                shoppingCartItemRepository
                        .deleteByUser_IdAndProductIdAndSize(
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

        shoppingCartItemRepository.deleteAllByUser_Id(userId);
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


   
}