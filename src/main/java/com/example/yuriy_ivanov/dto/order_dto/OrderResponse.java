package com.example.yuriy_ivanov.dto.order_dto;

import com.example.yuriy_ivanov.entities.LineItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private List<LineItem> lineItems;
}
