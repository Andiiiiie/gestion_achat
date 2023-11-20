package com.example.gestion_achat.controller;

import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;
import com.example.gestion_achat.repository.*;
import com.example.gestion_achat.service.PurchaseMetier;
import com.example.gestion_achat.service.PurchaseService;
import com.example.gestion_achat.service.SupplierService;
import lombok.Getter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final RequestRepository requestRepository;

    private final ProductRepository productRepository;

    private final SuppierRepository suppierRepository;
    private final PriceRepository priceRepository;


    public PurchaseController(PurchaseRepository purchaseRepository, RequestRepository requestRepository, ProductRepository productRepository, SuppierRepository suppierRepository,
                              PriceRepository priceRepository) {
        this.purchaseRepository = purchaseRepository;
        this.requestRepository = requestRepository;
        this.productRepository = productRepository;
        this.suppierRepository = suppierRepository;
        this.priceRepository = priceRepository;
    }

    @GetMapping("purchase/list")
    public String lister_a_acheter(Model model)
    {
        PurchaseService purchaseService=new PurchaseService(purchaseRepository,requestRepository);
        List<PurchaseMetier> purchaseMetierList=purchaseService.generate_Purchase();
        model.addAttribute("purchaseMetierList",purchaseMetierList);
        return "purchase/list";
    }

    @GetMapping("purchase/details/{id}")
    public String details(Model model, @PathVariable int id)
    {
        SupplierService supplierService=new SupplierService(suppierRepository,productRepository,priceRepository);
        Optional<Product> product1=productRepository.findById(id);
        Product product=product1.get();
        PurchaseService purchaseService=new PurchaseService(purchaseRepository,requestRepository);
        PurchaseMetier purchaseMetier=purchaseService.get_purchase(product);
        List<Supplier> supplierList=suppierRepository.findAll();
        List<Supplier> moins_chers=supplierService.liste_plus_mora(product);
        model.addAttribute("purchase",purchaseMetier);
        //model.addAttribute("supplierList",supplierList);
        model.addAttribute("supplierList",moins_chers);
        return "purchase/details";
    }





}
