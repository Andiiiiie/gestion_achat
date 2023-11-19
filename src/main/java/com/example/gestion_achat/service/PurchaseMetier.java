package com.example.gestion_achat.service;

import com.example.gestion_achat.entity.Purchase;
import com.example.gestion_achat.entity.Request;

import java.util.List;

public class PurchaseMetier {
    Purchase purchase;
    List<Request> requestList;

    public PurchaseMetier() {
    }

    public PurchaseMetier(Purchase purchase, List<Request> requestList) {
        this.purchase = purchase;
        this.requestList = requestList;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }
}
