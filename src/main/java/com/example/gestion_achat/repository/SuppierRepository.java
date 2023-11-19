package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppierRepository extends JpaRepository<Supplier, Integer> {
}