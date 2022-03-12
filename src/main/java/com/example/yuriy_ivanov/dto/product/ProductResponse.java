package com.example.yuriy_ivanov.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String brand;
    private String type;
    private Integer volume;
    private Integer count;
    private Float price;
}
