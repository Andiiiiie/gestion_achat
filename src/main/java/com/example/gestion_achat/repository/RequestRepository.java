package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.state = ?1")
    List<Request> findByState(Integer state);


    @Query("SELECT DISTINCT r.product FROM Request r WHERE r.purchase IS NULL and r.state=1")
    List<Object> findDistinctProductByPurchaseIsNull();

    @Query("SELECT  r FROM Request r WHERE r.purchase IS NULL and r.state=1 and r.product=?1")
    List<Request> get_commandes(Product product);
}