package com.example.yuriy_ivanov.dto.product_dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {
    private String brand;
    private String type;
    private Integer volume;
    private Integer count;
    private Float price;
}
