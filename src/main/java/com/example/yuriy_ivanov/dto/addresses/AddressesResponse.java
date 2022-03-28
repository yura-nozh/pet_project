package com.example.yuriy_ivanov.dto.addresses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressesResponse {
    private Long id;
    private String country;
    private String city;
    private String street;
    private Integer houseNumber;
    private Long userId;
}
