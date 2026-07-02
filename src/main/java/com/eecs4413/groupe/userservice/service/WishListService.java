package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.exception.WishListItemAlreadyExistsException;
import com.eecs4413.groupe.userservice.model.WishListItem;
import com.eecs4413.groupe.userservice.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WishListService {

    private final WishListRepository _wishlistRepository;

    public WishListService(WishListRepository wishlistRepository) {
        this._wishlistRepository = wishlistRepository;
    }

    public List<WishListItem> getWishlistByUserId(UUID userId) {
        List<WishListItem> wishlist =  _wishlistRepository.findByUserId(userId);
        
        if (wishlist == null) {
        	throw new UserNotFoundException(userId);
        }
        
        return wishlist;
        
    }

    public WishListItem addProductToWishlist(UUID userId, UUID productId) {
        boolean alreadyExists = _wishlistRepository.existsByUserIdAndProductId(userId, productId);

        if (alreadyExists) {
        	throw new WishListItemAlreadyExistsException(userId, productId);
        }

        WishListItem wishlistItem = new WishListItem(userId, productId);
        return _wishlistRepository.save(wishlistItem);
    }

    @Transactional
    public void removeProductFromWishlist(UUID userId, UUID productId) {
        _wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
