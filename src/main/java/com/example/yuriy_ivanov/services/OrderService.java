package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.order.OrderRequest;
import com.example.yuriy_ivanov.dto.order.OrderResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.Order;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    private Cart findCart(@NotNull OrderRequest orderRequest) {
        return cartRepository.findByUserId(orderRequest.getUserId());
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Cart cart = findCart(orderRequest);
        Order order = new Order();

        order.setLineItems(cart.getLineItems());
        order.setUser(cart.getUser());
        cart.setLineItems(null);
        orderRepository.save(order);
        cartRepository.delete(cart);

        return objectMapper.convertValue(order, OrderResponse.class);
    }
}
