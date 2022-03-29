package com.example.yuriy_ivanov.entities;

import com.example.yuriy_ivanov.dto.enums.Brand;
import com.example.yuriy_ivanov.dto.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "public", name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Integer volume; // bag volume

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer count;

    @Column(columnDefinition = "integer default 0")
    private Float price;
}
