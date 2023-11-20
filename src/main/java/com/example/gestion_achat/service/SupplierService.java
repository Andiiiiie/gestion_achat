package com.example.gestion_achat.service;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;
import com.example.gestion_achat.model.Proforma;
import com.example.gestion_achat.repository.PriceRepository;
import com.example.gestion_achat.repository.ProductRepository;
import com.example.gestion_achat.repository.SuppierRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Service
public class SupplierService {
    SuppierRepository suppierRepository;
    ProductRepository productRepository;
    PriceRepository priceRepository;

    public SupplierService(SuppierRepository suppierRepository, ProductRepository productRepository, PriceRepository priceRepository) {
        this.suppierRepository = suppierRepository;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    public Proforma get_Proformat(Supplier supplier, Product product, int quantite,double tva)
    {
        Proforma proforma=new Proforma();
        proforma.setSupplier(supplier);
        proforma.setProduct(product);
        proforma.setQuantity(quantite);
        proforma.setPrice(getMostRecent(product,supplier));
        proforma.setTVA(tva);
        proforma.setPrix();
        return proforma;
    }

    public Price getMostRecent(Product product,Supplier supplier)
    {
        List<Price> prices = priceRepository.findRecentPrices(product,supplier);
        return prices.get(0);
    }

    public List<Supplier> find_supplier(Product product)
    {
        List<Object> list=priceRepository.findSupplier(product);
        List<Supplier> supplierList=new ArrayList<>();
        for (Object o:list)
        {
            supplierList.add(Supplier.class.cast(o));
        }
        return supplierList;
    }

    public int get_Min(Product product,List<Supplier> supplier)
    {
        double temp=getMostRecent(product,supplier.get(0)).getUnitPrice();
        int tempS=0;
        int i=-1;
        for (Supplier supplier1:supplier)
        {
            i+=1;
            if(getMostRecent(product,supplier1).getUnitPrice()<temp)
            {
                temp=getMostRecent(product,supplier1).getUnitPrice();
                tempS=i;
            }
        }
        return tempS;
    }


    public List<Supplier> liste_plus_mora(Product product)
    {
        List<Supplier> supplierList=find_supplier(product);
        List<Supplier> milamina=new ArrayList<>();
        for (int i=0;i<=supplierList.size();i++)
        {
            int ind=get_Min(product,supplierList);
            milamina.add(supplierList.get(ind));
            supplierList.remove(ind);
        }
        return milamina;
    }


}
