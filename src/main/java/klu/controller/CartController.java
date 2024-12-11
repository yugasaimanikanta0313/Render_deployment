package klu.controller;

import klu.entitiy.Arts;
import klu.entitiy.Cart;
import klu.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{userId}/{artId}")
    public Cart addItemToCart(@PathVariable Long userId, @PathVariable Long artId, @RequestParam int quantity) {
        return cartService.addToCart(userId, artId, quantity);
    }


    @GetMapping("/user/{userId}")
    public List<Cart> getUserCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeItemFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearUserCart(@PathVariable Long userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{userId}/{cartItemId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        
        Cart updatedCartItem = cartService.updateCartItem(userId, cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }
}
