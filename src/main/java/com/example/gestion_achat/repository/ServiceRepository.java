package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}