package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/addItem")
    public ResponseEntity<CartResponse> addItem(@RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = cartService.addItem(cartRequest);
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/removeItem")
    public ResponseEntity<CartResponse> removeItem(@RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = cartService.removeItem(cartRequest);
        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping("/showCart/{userId}")
    public ResponseEntity<CartResponse> showUserCart(@PathVariable Long userId) {
        CartResponse cartResponse = cartService.showUserCart(userId);
        return ResponseEntity.ok(cartResponse);
    }

    //FIXED
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        cartService.destroy(id);
        return ResponseEntity.ok("Cart was successfully deleted.");
    }

}
