package klu.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import klu.entitiy.Arts;
import klu.entitiy.User;
import klu.entitiy.Wishlist;
import klu.repository.UserRepository;
import klu.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    public Wishlist addToWishlist(Long userId, Arts art) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create a new Wishlist entry
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setArt(art);

        // Save the wishlist entry
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getUserWishlist(Long userId) {
        // Find wishlist items by user ID
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        
        // If necessary, you can also fetch additional details here
        for (Wishlist wishlist : wishlists) {
            wishlist.getArt(); // This ensures the Arts object is fetched
        }
        
        return wishlists;
    }


    public void removeFromWishlist(Long wishlistId) {
        // Remove a wishlist entry by its ID
        wishlistRepository.deleteById(wishlistId);
    }
    public void clearWishlist(Long userId) {
        // Find all wishlist items for the user and delete them
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        wishlistRepository.deleteAll(wishlists);
    }

}