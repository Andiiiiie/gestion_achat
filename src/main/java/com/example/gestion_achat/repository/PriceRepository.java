package com.example.gestion_achat.repository;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    @Query("SELECT DISTINCT p.supplier FROM Price p WHERE p.product=?1")
    List<Object> findSupplier(Product product);

    @Query("select p from Price p order by p.price_date DESC")
    List<Price> findByOrderByPrice_dateDesc();

    @Query("SELECT p FROM Price p WHERE p.product = ?1 and p.supplier=?2 ORDER BY p.price_date DESC")
    List<Price> findRecentPrices(Product product,Supplier supplier);


}