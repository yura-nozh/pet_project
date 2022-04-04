package com.example.yuriy_ivanov.dto.product;

import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.entities.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductRequest {
    private Brand brand;
    private Type type;
    private Integer volume; //backpack volume in liters
    private Integer count;
    private Float price;
}
