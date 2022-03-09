package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    LineItem findByProductId(Product product);
}
