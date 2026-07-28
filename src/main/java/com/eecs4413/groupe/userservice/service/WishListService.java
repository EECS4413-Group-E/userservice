package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.client.CatalogueClient;
import com.eecs4413.groupe.userservice.client.model.response.ListingExistenceResponse;
import com.eecs4413.groupe.userservice.exception.ListingNotFoundNotException;
import com.eecs4413.groupe.userservice.exception.UserNotFoundException;
import com.eecs4413.groupe.userservice.exception.WishListItemAlreadyExistsException;
import com.eecs4413.groupe.userservice.model.entity.WishListItem;
import com.eecs4413.groupe.userservice.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishListService {

    private final WishListRepository _wishlistRepository;

    private final CatalogueClient _catalogueClient;

    public WishListService(WishListRepository wishlistRepository, CatalogueClient catalogueClient) {
        this._wishlistRepository = wishlistRepository;
        this._catalogueClient = catalogueClient;
    }

    public List<WishListItem> getWishlistByUserId(UUID userId) {
        List<WishListItem> wishlist = _wishlistRepository.findByUserId(userId);

        if (wishlist == null) {
            throw new UserNotFoundException(userId);
        }

        wishlist = removeNonExistingListingsFromWishlist(userId, wishlist);
        
        return wishlist;
        
    }

    public WishListItem addListingToWishlist(UUID userId, UUID listingId) {
        boolean alreadyExists = _wishlistRepository.existsByUserIdAndlistingId(userId, listingId);

        ListingExistenceResponse existenceResponse =  _catalogueClient.checkListingExistenceById(listingId);

        if (!existenceResponse.exists()) {
            throw new ListingNotFoundNotException(listingId);
        }

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

    private List<WishListItem> removeNonExistingListingsFromWishlist(UUID userId, List<WishListItem> wishlist) {
        List<UUID> listingIds = wishlist.stream().map(WishListItem::getListingId).toList();

        List<ListingExistenceResponse> existenceResponses = _catalogueClient.checkListingExistenceBatch(listingIds);

        Set<UUID> missingListingIds = existenceResponses.stream()
                .filter(response -> !response.exists())
                .map(ListingExistenceResponse::listingId)
                .collect(Collectors.toSet());

        missingListingIds.forEach(id -> _wishlistRepository.deleteByUserIdAndlistingId(userId, id));

        return wishlist.stream()
                .filter(item -> !missingListingIds.contains(item.getListingId()))
                .toList();
    }
}
