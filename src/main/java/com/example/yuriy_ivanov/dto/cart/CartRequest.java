package com.example.yuriy_ivanov.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartRequest {
    private Long productId;
    private Long userId;
}
