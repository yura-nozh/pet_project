package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.order.OrderRequest;
import com.example.yuriy_ivanov.dto.order.OrderResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Order;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.OrderRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    private Cart findCart(@NotNull OrderRequest orderRequest) {
        return cartRepository.findByUserId(orderRequest.getUserId());
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Cart cart = findCart(orderRequest);
        checkProductAvailability(cart);
        Order order = new Order();
        order.setLineItems(cart.getLineItems());
        order.setUser(cart.getUser());
        cart.setLineItems(null);
        orderRepository.save(order);
        cartRepository.delete(cart);

        return objectMapper.convertValue(order, OrderResponse.class);
    }

    public void checkProductAvailability(Cart cart) {
        List<LineItem> lineItems = cart.getLineItems();
        for (LineItem lineItem : lineItems) {
            int countInRepo = productRepository.getById(lineItem.getProduct().getId()).getCount();
            int countInCart = lineItem.getQuantity();
            if (countInCart > countInRepo) {
                throw new ServiceException("Unfortunately, there are not so many bags" + lineItem.getProduct().getBrand().getBrandName() + ". Reduce the quantity"
                        , TypicalError.NOT_SO_MANY_PRODUCTS);
            }
        }
    }
}
