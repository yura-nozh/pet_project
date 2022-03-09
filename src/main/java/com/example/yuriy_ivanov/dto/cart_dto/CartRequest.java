package com.example.yuriy_ivanov.dto.cart_dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartRequest {
    private Long productId;
    private Long userId;
}
