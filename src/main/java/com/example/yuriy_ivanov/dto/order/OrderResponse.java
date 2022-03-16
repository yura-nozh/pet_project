package com.example.yuriy_ivanov.dto.order;

import com.example.yuriy_ivanov.dto.line_item.LineItemResponse;
import com.example.yuriy_ivanov.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private List<LineItemResponse> lineItems;
    private User user;
}
