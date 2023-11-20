package com.example.gestion_achat.model;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;

public class Ligne {
    Product product;
    int quantite;

    Price price;

    double montant;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
}
