package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagsRepository extends JpaRepository<Bag, Long> {
}
