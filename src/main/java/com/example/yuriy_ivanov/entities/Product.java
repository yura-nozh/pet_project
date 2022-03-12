package com.example.yuriy_ivanov.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "public", name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer volume;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer count;

    @Column(columnDefinition = "integer default 0")
    private Float price;
}
