package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.Cart;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
    @NotNull Optional<Cart> findById(@NotNull Long id);
    Optional<Cart> getByUserId(Long userId);
}
