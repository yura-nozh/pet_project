package com.example.yuriy_ivanov.dto.BagsDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BagRequest {
    private String brand;
    private String type;
    private Integer volume;
    private Integer quantity;
}
