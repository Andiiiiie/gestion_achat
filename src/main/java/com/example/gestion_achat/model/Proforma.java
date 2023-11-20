package com.example.gestion_achat.model;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;

public class Proforma {
    Supplier supplier;
    Product product;
    int quantity;

    Price price;
    double TVA;
    double prixTTC;
    double prixHT;



    public double getPourcentage(double pourcent,double somme)
    {
        return (pourcent*somme)/100;
    }
    public void setPrix()
    {
        double TVA=getPourcentage(getTVA(),getPrice().getUnitPrice())*this.getQuantity();
        double HT=this.getPrice().getUnitPrice()*this.getQuantity();
        this.setPrixHT(HT);
        this.setPrixTTC(TVA+HT);
    }





    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public double getTVA() {
        return TVA;
    }

    public void setTVA(double TVA) {
        this.TVA = TVA;
    }

    public double getPrixTTC() {
        return prixTTC;
    }

    public void setPrixTTC(double prixTTC) {
        this.prixTTC = prixTTC;
    }

    public double getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(double prixHT) {
        this.prixHT = prixHT;
    }
}
