package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBrandByBrandName(String brandName);
}
