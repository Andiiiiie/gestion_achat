package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}