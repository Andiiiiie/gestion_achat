package com.example.gestion_achat.controller;

import com.example.gestion_achat.config.CustomUserDetails;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Request;
import com.example.gestion_achat.entity.Request_type;
import com.example.gestion_achat.entity.User;
import com.example.gestion_achat.repository.ProductRepository;
import com.example.gestion_achat.repository.RequestRepository;
import com.example.gestion_achat.repository.Request_typeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class RequestController {
    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;
    private final Request_typeRepository requestTypeRepository;

    public RequestController(ProductRepository productRepository, RequestRepository requestRepository, Request_typeRepository requestTypeRepository) {
        this.productRepository = productRepository;
        this.requestRepository = requestRepository;
        this.requestTypeRepository = requestTypeRepository;
    }

    @PostMapping("/request/ask")
    public ModelAndView request(@ModelAttribute("request") Request request, Model model) {
        if (request.getQuantity() <= 0) {
            return new ModelAndView("redirect:ask/quantite invalide");
        }
        request.setRequestdate(LocalDate.now());
        User user=get_connected();
        request.setUser_requester(user);
        request.setState(0);
        requestRepository.save(request);
        return new ModelAndView("redirect:ask");
    }

    public User get_connected()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails cUser = (CustomUserDetails) authentication.getPrincipal();
        User user = cUser.user;
        return user;
    }

    @GetMapping("/request/ask/{error}")
    public String request_forn(@ModelAttribute("request") Request request, Model model, @PathVariable String error) {
        List<Product> productList = productRepository.findAll();
        List<Request_type> request_typeList = requestTypeRepository.findAll();
        model.addAttribute("productList", productList);
        model.addAttribute("request_typeList", request_typeList);
        if (!error.equals("")) {
            model.addAttribute("error", error);
        }
        return "request/create";
    }

    @GetMapping("/request/list")
    public String list_request(Model model) {
        List<Request> requestList = requestRepository.findByState(0);
        model.addAttribute("requestList", requestList);
        return "request/list";
    }

    @GetMapping("/request/accept/{id}")
    public RedirectView accepter(Model model,@PathVariable int id)
    {
        Optional<Request> r=requestRepository.findById(id);
        Request request=r.get();
        request.setState(1);
        request.setUser_validator(get_connected());
        requestRepository.save(request);
        RedirectView redirectView=new RedirectView("/request/list");
        return redirectView;
    }

    @GetMapping("/request/refuse/{id}")
    public RedirectView refuser(Model model, @PathVariable int id)
    {
        Optional<Request> r=requestRepository.findById(id);
        Request request=r.get();
        request.setState(-1);
        request.setUser_validator(get_connected());
        requestRepository.save(request);
        RedirectView redirectView=new RedirectView("/request/list");
        return redirectView;
    }

}
