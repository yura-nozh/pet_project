package com.example.yuriy_ivanov.dto.product;

import com.example.yuriy_ivanov.dto.enums.Brand;
import com.example.yuriy_ivanov.dto.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {
    private Brand brand;
    private Type type;
    private Integer volume; //backpack volume in liters
    private Integer count;
    private Float price;
}
