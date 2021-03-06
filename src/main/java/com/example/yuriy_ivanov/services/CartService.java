package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.LineItemRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        createLineItem(cart, product, cartRequest.getQuantity());

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

    public CartResponse showUserCart(Long userId) {
        Optional<Cart> cart = cartRepository.getByUserId(userId);
        Cart desiredCart = cart.orElseThrow(() -> new ServiceException("CART NOT FOUND, PLEASE< ADD FIRST ITEM", TypicalError.CART_NOT_FOUND));

        return objectMapper.convertValue(desiredCart, CartResponse.class);
    }

    public void destroy(Long id) {
        cartRepository.deleteById(id);
    }

    private User findUser(CartRequest cartRequest) throws ServiceException {
        Optional<User> user = userRepository.findById(cartRequest.getUserId());
        return user.orElseThrow(()-> new ServiceException("USER NOT FOUND", TypicalError.USER_NOT_FOUND));
    }

    private Product findProduct(CartRequest cartRequest) throws ServiceException {
        Optional<Product> product = productRepository.findById(cartRequest.getProductId());

        if(product.isPresent()) {
            return product.get();
        }

        throw new ServiceException("Product not found", TypicalError.PRODUCT_NOT_FOUND);
    }

    private Cart findCart(User user) throws ServiceException {
        Optional<Cart> cart = cartRepository.getByUserId(user.getId());
        return cart.orElseThrow(() -> new ServiceException("PRODUCT NOT FOUND", TypicalError.PRODUCT_NOT_FOUND));
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

    private void createLineItem(Cart cart, Product product, Integer qty) {
        if (cart.getLineItems() == null) {
            LineItem lineItem = initLineItem(product, qty);
            List<LineItem> lineItems= new ArrayList<>();
            lineItems.add(lineItem);
            lineItem.setCart(cart);
            cart.setLineItems(lineItems);
        }
        else {
            updateLineItemQuantityOrCreateLineItem(cart, product, qty);
        }
    }

    private @NotNull LineItem initLineItem(Product product, Integer qty) {
        LineItem lineItem = new LineItem();
        lineItem.setQuantity(qty);
        lineItem.setProduct(product);

        return lineItem;
    }

    private void updateLineItemQuantityOrCreateLineItem(@NotNull Cart cart, Product product, Integer qty) {
        for (LineItem item : cart.getLineItems()) {
            if (Objects.equals(item.getProduct().getId(), product.getId())) {
                item.setQuantity(item.getQuantity() + qty);
            }
            else {
                LineItem lineItem = initLineItem(product, qty);
                List<LineItem> lineItems= new ArrayList<>();
                lineItems.add(lineItem);
                lineItem.setCart(cart);
                cart.setLineItems(lineItems);
            }
        }
    }

    private void removeLineItem(@NotNull Cart cart, Product product) {
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

    private @NotNull LineItem findLineItem(@NotNull List<LineItem> lineItems, Product product) throws ServiceException {
        for(LineItem item : lineItems) {
            if(Objects.equals(item.getProduct().getId(), product.getId())) {
                return item;
            }
        }
        throw new ServiceException("Item not in cart", TypicalError.BAD_REQUEST);
    }
}
