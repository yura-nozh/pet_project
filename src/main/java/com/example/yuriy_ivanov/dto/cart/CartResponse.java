package com.example.yuriy_ivanov.dto.cart;

import com.example.yuriy_ivanov.dto.line_item.LineItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private List<LineItemResponse> lineItems;
}
