package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.cart_dto.CartRequest;
import com.example.yuriy_ivanov.dto.cart_dto.CartResponse;
import com.example.yuriy_ivanov.dto.product_dto.ProductRequest;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.LineItemRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final LineItemRepository lineItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public CartResponse addItem(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Product product = findProduct(cartRequest);
        Cart cart = findOrCreateCart(user);
        createLineItem(cart, product);

        return objectMapper.convertValue(cart, CartResponse.class);
    }

    public CartResponse removeItem(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Product product = findProduct(cartRequest);
        Cart cart = findCart(user);
        LineItem lineItem = findLineItem(cart.getLineItems(), product);
        removeLineItem(lineItem);

        return objectMapper.convertValue(cart, CartResponse.class);
    }

    public void destroy(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Cart cart = findCart(user);
        cartRepository.delete(cart);
    }

    private User findUser(CartRequest cartRequest) {
        return userRepository.getById(cartRequest.getUserId());
    }

    private Product findProduct(CartRequest cartRequest) {
        return productRepository.getById(cartRequest.getProductId());
    }

    private Cart findCart(User user) {
        return cartRepository.findByUserId(user.getId());
    }

    private Cart findOrCreateCart(User user) {
        Optional<Cart> cartFromDb = cartRepository.getByUserId(user.getId());
        Cart cart = null;

        if(cartFromDb.isPresent()) {
            cart = cartFromDb.get();
        } else {
            Cart newCart =  new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
            cart = newCart;
        }
        return cart;
    }

    private LineItem findLineItem(List<LineItem> lineItems, Product product) {
        LineItem lineItem = null;
        for(LineItem item : lineItems) {
            if(Objects.equals(item.getProduct().getId(), product.getId())) {
                lineItem = item;
            }
        }

        if (lineItem == null) {
            throw new RuntimeException();
        }

        return lineItem;
    }

    private void createLineItem(Cart cart, Product product) {
        LineItem lineItem = null;

        for (LineItem item : cart.getLineItems()) {
            if (Objects.equals(item.getProduct().getId(), product.getId())) {
                lineItem = item;
            }
        }

        if (lineItem != null ) {
            lineItem.setQuantity(lineItem.getQuantity() + 1);
            lineItemRepository.save(lineItem);
        } else {
            lineItem = new LineItem();
            lineItem.setCart(cart);
            lineItem.setProduct(product);
            lineItemRepository.save(lineItem);
        }

        lineItemRepository.save(lineItem);
    }

    private void removeLineItem(LineItem lineItem) {
        if(lineItem.getQuantity() > 1) {
            lineItem.setQuantity(lineItem.getQuantity() - 1);
            lineItemRepository.save(lineItem);
        }
        else {
            lineItemRepository.delete(lineItem);
        }
    }

}
