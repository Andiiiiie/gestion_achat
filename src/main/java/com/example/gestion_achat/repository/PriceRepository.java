package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {
}