package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.cart_dto.CartRequest;
import com.example.yuriy_ivanov.dto.order_dto.OrderRequest;
import com.example.yuriy_ivanov.dto.order_dto.OrderResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.Order;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.LineItemRepository;
import com.example.yuriy_ivanov.repositories.OrderRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

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

    private Cart findCart(OrderRequest orderRequest) {
        return cartRepository.findByUserId(orderRequest.getUserId());
    }

}
