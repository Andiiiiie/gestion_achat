package com.example.gestion_achat.controller;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;
import com.example.gestion_achat.repository.PriceRepository;
import com.example.gestion_achat.repository.ProductRepository;
import com.example.gestion_achat.repository.SuppierRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Controller
public class PriceController {
    PriceRepository priceRepository;
    SuppierRepository supplierRepository;
    ProductRepository productRepository;

    public PriceController(PriceRepository priceRepository, SuppierRepository supplierRepository, ProductRepository productRepository) {
        this.priceRepository = priceRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/price/list")
    public String list(Model model)
    {
        List<Price> priceList=priceRepository.findByOrderByPrice_dateDesc();
        model.addAttribute("priceList",priceList);
        return "price/list";
    }
    @GetMapping("price/create")
    public String create(@ModelAttribute("price") Price price, Model model)
    {
        List<Product> productList=productRepository.findAll();
        List<Supplier> supplierList=supplierRepository.findAll();
        model.addAttribute("productList",productList);
        model.addAttribute("supplierList",supplierList);
        return "price/create";
    }
    @PostMapping("price/create")
    public String save(@ModelAttribute("price") Price price,Model model)
    {
//        price.setPrice_date(LocalDate.now());
        priceRepository.save(price);
        return "redirect:/price/list";
    }


}
