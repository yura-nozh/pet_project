package com.example.yuriy_ivanov.entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "public", name = "addresses")
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
