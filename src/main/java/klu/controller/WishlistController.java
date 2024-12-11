package klu.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import klu.entitiy.Arts;
import klu.entitiy.Wishlist;
import klu.service.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // Add item to wishlist
    @PostMapping("/add/{userId}")
    public Wishlist addItemToWishlist(@PathVariable Long userId, @RequestBody Arts art) {
        return wishlistService.addToWishlist(userId, art);
    }

    // Get wishlist of a user
    @GetMapping("/user/{userId}")
    public List<Wishlist> getUserWishlist(@PathVariable Long userId) {
        return wishlistService.getUserWishlist(userId);
    }

    // Remove item from wishlist by wishlist item ID
    @DeleteMapping("/remove/{wishlistItemId}")
    public void removeItemFromWishlist(@PathVariable Long wishlistItemId) {
        wishlistService.removeFromWishlist(wishlistItemId);
    }
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearUserWishlist(@PathVariable Long userId) {
        wishlistService.clearWishlist(userId);
        return ResponseEntity.noContent().build(); // Return a 204 No Content response
    }


}
