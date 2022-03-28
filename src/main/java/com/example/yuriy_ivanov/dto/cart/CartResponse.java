package com.example.yuriy_ivanov.dto.cart;

import com.example.yuriy_ivanov.dto.line_item.LineItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private List<LineItemResponse> lineItems;
}
