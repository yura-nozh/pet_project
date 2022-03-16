package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.order.OrderRequest;
import com.example.yuriy_ivanov.dto.order.OrderResponse;
import com.example.yuriy_ivanov.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
}
