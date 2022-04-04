package com.example.yuriy_ivanov.dto.product;

import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.entities.Brand;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductResponse {
    private Long id;
    private Brand brand;
    private Type type;
    private Integer volume;
    private Integer count;
    private Float price;
}
