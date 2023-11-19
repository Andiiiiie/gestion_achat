package com.example.gestion_achat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "order_confirmation")
public class Order_confirmation {
    @Column(name = "state")
    private Integer state;

    @Column(name = "delivery")
    private Integer delivery;

    @ManyToOne
    @JoinColumn(name = "user_validator_id")
    private User userValidator;

    @ManyToOne
    @JoinColumn(name = "price_id")
    private Price price;

    @Column(name = "quantity")
    private String quantity;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "prix_ttc")
    private Double prixTTC;

    @Column(name = "prix_ht")
    private Double prixHT;

    @Column(name = "tva")
    private Double tva;

    @Column(name = "date_confirmation")
    private LocalDate dateConfirmation;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

}