package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.entities.Brand;
import com.example.yuriy_ivanov.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public Brand addNewBrand(String brandName) {
        Optional<Brand> brand1 = brandRepository.findBrandByBrandName(brandName);
        if(brand1.isPresent()) {
            return brand1.get();
        }
        else {
            Brand brand = new Brand();
            brand.setBrandName(brandName);
            brandRepository.save(brand);
            return brand;
        }
    }
}
