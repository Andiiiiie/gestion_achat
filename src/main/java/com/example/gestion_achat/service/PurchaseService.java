package com.example.gestion_achat.service;

import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Purchase;
import com.example.gestion_achat.entity.Request;
import com.example.gestion_achat.repository.PurchaseRepository;
import com.example.gestion_achat.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final RequestRepository requestRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, RequestRepository requestRepository) {
        this.purchaseRepository = purchaseRepository;
        this.requestRepository = requestRepository;
    }

    public List<PurchaseMetier> generate_Purchase()
    {
        List<Product> productList=get_products();
        List<PurchaseMetier> purchaseMetierList=new ArrayList<>();
        for (Product product:productList)
        {
            purchaseMetierList.add(get_purchase(product));
        }
        return purchaseMetierList;
    }

    public List<Product> get_products()
    {
        List<Object> list=requestRepository.findDistinctProductByPurchaseIsNull();
        List<Product> productList=new ArrayList<>();
        for (Object o:list)
        {
            productList.add(Product.class.cast(o));
        }
        return productList;
    }

    public int getQuantity(List<Request> list)
    {
        int total=0;
        for (Request request:list)
        {
            total+=request.getQuantity();
        }
        return total;
    }


    public PurchaseMetier get_purchase(Product product)
    {
        PurchaseMetier purchaseMetier=new PurchaseMetier();
        List<Request> requestList=requestRepository.get_commandes(product);
        purchaseMetier.setRequestList(requestList);
        Purchase purchase=new Purchase();
        purchase.setState(0);
        purchase.setProduct(product);
        purchase.setQuantity(getQuantity(requestList));
        purchaseMetier.setPurchase(purchase);
        return purchaseMetier;
    }
}
