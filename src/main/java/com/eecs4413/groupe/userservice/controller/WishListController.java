package com.eecs4413.groupe.userservice.controller;

import com.eecs4413.groupe.userservice.model.WishListItem;
import com.eecs4413.groupe.userservice.service.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/wishlist")
public class WishListController {

    private final WishListService _wishlistService;

    public WishListController(WishListService wishlistService) {
        _wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishListItem>> getWishlistByUserId(@PathVariable UUID userId) {
        List<WishListItem> wishlist = _wishlistService.getWishlistByUserId(userId);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishListItem> addProductToWishlist(
            @PathVariable UUID userId,
            @PathVariable UUID productId) {

        WishListItem wishlistItem =
                _wishlistService.addProductToWishlist(userId, productId);

        return new ResponseEntity<>(wishlistItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(
            @PathVariable UUID userId,
            @PathVariable UUID productId) {

        _wishlistService.removeProductFromWishlist(userId, productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}