package com.example.gestion_achat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "request")
public class Request {
    @ManyToOne
    @JoinColumn(name = "user_requester_id")
    private User user_requester;

    @ManyToOne
    @JoinColumn(name = "request_type_id")
    private Request_type request_type;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @Column(name = "requestdate")
    private LocalDate requestdate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_validator_id")
    private User user_validator;

}