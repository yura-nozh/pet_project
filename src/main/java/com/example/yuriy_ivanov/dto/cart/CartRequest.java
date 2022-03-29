package com.example.yuriy_ivanov.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartRequest {
    private Long productId;
    private Long userId;
    private Integer quantity;
}
