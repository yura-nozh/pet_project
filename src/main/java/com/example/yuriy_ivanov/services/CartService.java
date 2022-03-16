package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.LineItemRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final LineItemRepository lineItemRepository;

    public CartResponse addItem(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Product product = findProduct(cartRequest);
        Cart cart = findOrCreateCart(user);
        createLineItem(cart, product);

        cartRepository.save(cart);

        return objectMapper.convertValue(cart, CartResponse.class);
    }

    public CartResponse removeItem(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Product product = findProduct(cartRequest);
        Cart cart = findCart(user);
        removeLineItem(cart, product);

        cartRepository.save(cart);

        return objectMapper.convertValue(cart, CartResponse.class);
    }

    public void destroy(CartRequest cartRequest) {
        User user = findUser(cartRequest);
        Cart cart = findCart(user);
        cartRepository.delete(cart);
    }

    private User findUser(CartRequest cartRequest) {
        Optional<User> user = userRepository.findById(cartRequest.getUserId());

        if(user.isPresent()) {
            return user.get();
        }

        throw new RuntimeException();
    }

    private Product findProduct(CartRequest cartRequest) {
        Optional<Product> product = productRepository.findById(cartRequest.getProductId());

        if(product.isPresent()) {
            return product.get();
        }

        throw new RuntimeException();
    }

    private Cart findCart(User user) {
        return cartRepository.findByUserId(user.getId());
    }

    private Cart findOrCreateCart(User user) {
        Optional<Cart> cart = cartRepository.getByUserId(user.getId());

        if(cart.isPresent()) {
            return cart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);

            return newCart;
        }
    }

    private void createLineItem(Cart cart, Product product) {
        if (cart.getLineItems() == null) {
            LineItem lineItem = initLineItem(product);
            List<LineItem> lineItems= new ArrayList<>();
            lineItems.add(lineItem);
            lineItem.setCart(cart);
            cart.setLineItems(lineItems);
        }
        // TODO: 12.03.2022 add find product lineitem 
        else {
            updateLineItemQuantity(cart, product);
        }
    }

    private LineItem initLineItem(Product product) {
        LineItem lineItem = new LineItem();
        lineItem.setQuantity(1);
        lineItem.setProduct(product);

        return lineItem;
    }

    private void updateLineItemQuantity(Cart cart, Product product) {
        for (LineItem item : cart.getLineItems()) {
            if (Objects.equals(item.getProduct().getId(), product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
            }
        }
    }

    private void removeLineItem(Cart cart, Product product) {
        List<LineItem> lineItems = cart.getLineItems();
        LineItem lineItem = findLineItem(lineItems, product);

        Integer quantity = lineItem.getQuantity();
        if(quantity != null && quantity > 1) {
            lineItem.setQuantity(quantity - 1);
        } else {
            lineItems.remove(lineItem);
            cart.setLineItems(lineItems);
            lineItemRepository.delete(lineItem);
        }
    }

    private LineItem findLineItem(List<LineItem> lineItems, Product product) {
        for(LineItem item : lineItems) {
            if(Objects.equals(item.getProduct().getId(), product.getId())) {
                return item;
            }
        }

        throw new RuntimeException();
    }
}
