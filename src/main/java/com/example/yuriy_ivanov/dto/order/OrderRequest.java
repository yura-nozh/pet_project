package com.example.yuriy_ivanov.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderRequest {
    private Long userId;
}
