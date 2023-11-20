package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Order_confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Order_confirmationRepository extends JpaRepository<Order_confirmation, Integer> {
    @Query("select o from Order_confirmation o where o.state = ?1")
    List<Order_confirmation> findByState(Integer state);

}