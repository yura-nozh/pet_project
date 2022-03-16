package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    LineItem findLineItemByProductId(Long id);
}
