package klu.service;

import klu.entitiy.Arts;
import klu.entitiy.Cart;
import klu.entitiy.User;
import klu.repository.CartRepository;
import klu.repository.UserRepository;
import klu.repository.ArtsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtsRepository artsRepository;

    public Cart addToCart(Long userId, Long artId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Arts art = artsRepository.findById(artId)
                .orElseThrow(() -> new IllegalArgumentException("Art not found"));

        Cart cartItem = new Cart();
        cartItem.setUser(user);
        cartItem.setArt(art);
        cartItem.setArtTitle(art.getArtTitle());
        cartItem.setQuantity(quantity);
        cartItem.setPrice(art.getPrice() * quantity);

        return cartRepository.save(cartItem);
    }

    public List<Cart> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void removeFromCart(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void clearUserCart(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }
    @Transactional
    public Cart updateCartItem(Long userId, Long cartItemId, int quantity) {
        Cart cartItem = cartRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }
}
