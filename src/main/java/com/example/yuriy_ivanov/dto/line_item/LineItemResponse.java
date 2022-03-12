package com.example.yuriy_ivanov.dto.line_item;

import com.example.yuriy_ivanov.dto.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LineItemResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
}
