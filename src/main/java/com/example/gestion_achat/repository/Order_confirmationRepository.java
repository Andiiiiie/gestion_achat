package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Order_confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Order_confirmationRepository extends JpaRepository<Order_confirmation, Integer> {
}