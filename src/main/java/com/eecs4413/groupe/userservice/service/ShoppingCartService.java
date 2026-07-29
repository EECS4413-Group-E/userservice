package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.client.CatalogueClient;
import com.eecs4413.groupe.userservice.client.model.request.ProductExistenceRequest;
import com.eecs4413.groupe.userservice.client.model.response.ProductExistenceResponse;
import com.eecs4413.groupe.userservice.exception.InvalidShoppingCartQuantityException;
import com.eecs4413.groupe.userservice.exception.ProductNotFoundNotException;
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
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCartItemRepository _shoppingCartItemRepository;
    private final UserRepository _userRepository;

    private final CatalogueClient _catalogueClient;

    public ShoppingCartService(
            ShoppingCartItemRepository shoppingCartItemRepository,
            UserRepository userRepository,
            CatalogueClient catalogueClient
    ) {
        this._shoppingCartItemRepository = shoppingCartItemRepository;
        this._userRepository = userRepository;
        _catalogueClient = catalogueClient;
    }

    public List<ShoppingCartItemResponse> getCart(UUID userId) {
        validateUserExists(userId);

        List<ShoppingCartItem> shoppingCartItems = _shoppingCartItemRepository.findAllByUserId(userId);

        shoppingCartItems = removeNonExistingProductsFromStoredCart(userId, shoppingCartItems);

        return shoppingCartItems
                .stream()
                .map(ShoppingCartItemResponse::from)
                .toList();
    }

    @Transactional
    public ShoppingCartItemResponse addItem(UUID userId, ShoppingCartItemRequest request) {
        User user = _userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        validateProductExistence(userId, request.productId(), request.size());
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

        List<ShoppingCartItemRequest> filteredRequest = removeNonExistingProductsFromRequest(request);

        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

        for(ShoppingCartItemRequest itemRequest : filteredRequest) {
            validateQuantity(itemRequest.quantity());

            shoppingCartItems.add(new ShoppingCartItem(
                    user,
                    itemRequest.productId(),
                    itemRequest.size(),
                    itemRequest.quantity()));
        }

        return _shoppingCartItemRepository.saveAllAndFlush(shoppingCartItems);
    }

    public ShoppingCartItemResponse updateQuantity(UUID userId, ShoppingCartItemRequest request) {
        validateUserExists(userId);
        validateQuantity(request.quantity());
        validateProductExistence(userId, request.productId(), request.size());

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

    private void validateProductExistence(UUID userId, UUID productId, Size size) {
        ProductExistenceResponse existenceResponse = _catalogueClient.checkProductSizeExistence(
                new ProductExistenceRequest(productId, size));

        if (!existenceResponse.exists()) {
            _shoppingCartItemRepository.deleteByUserIdAndProductIdAndSize(userId, productId, size);
            throw new ProductNotFoundNotException(productId, size);
        }
    }

    private List<ShoppingCartItem> removeNonExistingProductsFromStoredCart(UUID userId, List<ShoppingCartItem> shoppingCartItems) {
        List<ProductExistenceRequest> existencesRequest = shoppingCartItems.stream()
                .map(item -> new ProductExistenceRequest(item.getProductId(), item.getSize()))
                .toList();

        List<ProductExistenceResponse> existencesResponse = _catalogueClient.checkProductSizeExistenceBatch(existencesRequest);

        Set<ProductKey> missingProducts = existencesResponse.stream()
                .filter(response -> !response.exists())
                .map(response -> new ProductKey(response.productId(), response.size()))
                .collect(Collectors.toSet());

        missingProducts.forEach(productKey ->
                _shoppingCartItemRepository.deleteByUserIdAndProductIdAndSize(userId, productKey.productId, productKey.size));
        _shoppingCartItemRepository.flush();

        return shoppingCartItems.stream()
                .filter(item -> !missingProducts.contains(new ProductKey(item.getProductId(), item.getSize())))
                .toList();
    }

    private List<ShoppingCartItemRequest> removeNonExistingProductsFromRequest(List<ShoppingCartItemRequest> shoppingCartItems) {
        List<ProductExistenceRequest> existencesRequest = shoppingCartItems.stream()
                .map(item -> new ProductExistenceRequest(item.productId(), item.size()))
                .toList();

        List<ProductExistenceResponse> existencesResponse = _catalogueClient.checkProductSizeExistenceBatch(existencesRequest);

        Set<ProductKey> missingProducts = existencesResponse.stream()
                .filter(response -> !response.exists())
                .map(response -> new ProductKey(response.productId(), response.size()))
                .collect(Collectors.toSet());

        return shoppingCartItems.stream()
                .filter(item -> !missingProducts.contains(new ProductKey(item.productId(), item.size())))
                .toList();
    }

    private record ProductKey(UUID productId, Size size) {}
}