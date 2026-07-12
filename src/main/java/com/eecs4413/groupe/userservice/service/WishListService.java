package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.exception.WishListItemAlreadyExistsException;
import com.eecs4413.groupe.userservice.model.entity.WishListItem;
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

    public WishListItem addListingToWishlist(UUID userId, UUID listingId) {
        boolean alreadyExists = _wishlistRepository.existsByUserIdAndlistingId(userId, listingId);

        if (alreadyExists) {
        	throw new WishListItemAlreadyExistsException(userId, listingId);
        }

        WishListItem wishlistItem = new WishListItem(userId, listingId);
        return _wishlistRepository.save(wishlistItem);
    }

    @Transactional
    public void removeListingFromWishlist(UUID userId, UUID listingId) {
        _wishlistRepository.deleteByUserIdAndlistingId(userId, listingId);
    }
}
