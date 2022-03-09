package com.example.yuriy_ivanov.dto.cart_dto;

import com.example.yuriy_ivanov.entities.LineItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private List<LineItem> lineItems;
}
