package com.example.yuriy_ivanov.repositories;

import com.example.yuriy_ivanov.entities.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Long> {
}
