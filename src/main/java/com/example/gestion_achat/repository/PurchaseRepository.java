package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Purchase;
import com.example.gestion_achat.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}