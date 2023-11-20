package com.example.gestion_achat.controller;

import com.example.gestion_achat.config.CustomUserDetails;
import com.example.gestion_achat.entity.*;
import com.example.gestion_achat.model.Proforma;
import com.example.gestion_achat.repository.*;
import com.example.gestion_achat.service.PurchaseMetier;
import com.example.gestion_achat.service.PurchaseService;
import com.example.gestion_achat.service.SupplierService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Getter
@Setter
@Controller
public class Order_confirmationController {
    private final PurchaseRepository purchaseRepository;
    private final RequestRepository requestRepository;

    private final ProductRepository productRepository;

    private final SuppierRepository suppierRepository;
    private final PriceRepository priceRepository;
    private final Order_confirmationRepository order_confirmationRepository;


    public Order_confirmationController(PurchaseRepository purchaseRepository, RequestRepository requestRepository, ProductRepository productRepository, SuppierRepository suppierRepository, PriceRepository priceRepository, Order_confirmationRepository order_confirmationRepository) {
        this.purchaseRepository = purchaseRepository;
        this.requestRepository = requestRepository;
        this.productRepository = productRepository;
        this.suppierRepository = suppierRepository;
        this.priceRepository = priceRepository;
        this.order_confirmationRepository = order_confirmationRepository;
    }

    public User get_connected()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails cUser = (CustomUserDetails) authentication.getPrincipal();
        User user = cUser.user;
        return user;
    }
    @GetMapping("order/list")
    public String get_list(Model model)
    {
        List<Order_confirmation> orderConfirmationList=order_confirmationRepository.findByState(0);
        model.addAttribute("orderConfirmationList",orderConfirmationList);
        return "/order_confirmation/list";
    }

    @GetMapping("order/finance/list")
    public String get_list_finance(Model model)
    {
        List<Order_confirmation> orderConfirmationList=order_confirmationRepository.findByState(1);
        model.addAttribute("orderConfirmationList",orderConfirmationList);
        return "/order_confirmation/list";
    }


    public void refuser(Order_confirmation orderConfirmation)
    {
        orderConfirmation.getPurchase().setState(-1);
        purchaseRepository.save(orderConfirmation.getPurchase());
        List<Request> requestList=requestRepository.findByPurchase(orderConfirmation.getPurchase());
        for (Request request:requestList)
        {
            request.setPurchase(null);
            request.setUser_validator(null);
            requestRepository.save(request);
        }
    }


    @GetMapping("order/DG/list")
    public String get_list_direction(Model model)
    {
        List<Order_confirmation> orderConfirmationList=order_confirmationRepository.findByState(2);
        model.addAttribute("orderConfirmationList",orderConfirmationList);
        return "/order_confirmation/list";
    }





    @GetMapping("order/generate/{id_product}")
    public String generate_order_confirmation(Model model, @PathVariable int id_product)
    {
        PurchaseService service=new PurchaseService( purchaseRepository,requestRepository);
        PurchaseMetier purchaseMetier=service.get_purchase(productRepository.findById(id_product).get());
        purchaseRepository.save(purchaseMetier.getPurchase());
        for (Request request:purchaseMetier.getRequestList())
        {
            request.setPurchase(purchaseMetier.getPurchase());
            requestRepository.save(request);
        }
        SupplierService supplierService=new SupplierService(suppierRepository,productRepository,priceRepository);
        Supplier supplier=supplierService.liste_plus_mora(purchaseMetier.getPurchase().getProduct()).get(0);
        Order_confirmation orderConfirmation=new Order_confirmation();
        orderConfirmation.setProduct(purchaseMetier.getPurchase().getProduct());
        orderConfirmation.setPurchase(purchaseMetier.getPurchase());
        orderConfirmation.setState(0);
        orderConfirmation.setSupplier(supplier);
        orderConfirmation.setQuantity(purchaseMetier.getPurchase().getQuantity());

        Proforma proforma=supplierService.get_Proformat(orderConfirmation.getSupplier(),orderConfirmation.getProduct(),orderConfirmation.getQuantity(),20);

        orderConfirmation.setPrixHT(proforma.getPrixHT());
        orderConfirmation.setPrixTTC(proforma.getPrixTTC());
        orderConfirmation.setTva(proforma.getPrixTTC()-proforma.getPrixHT());
        orderConfirmation.setDelivery(30);
        orderConfirmation.setPrice(proforma.getPrice());

        order_confirmationRepository.save(orderConfirmation);
        model.addAttribute("orderConfirmation",orderConfirmation);
        return "/order_confirmation/details";
    }

    @GetMapping("order/validate/{id}")
    public RedirectView validate(Model model, @PathVariable int id)
    {
        Order_confirmation orderConfirmation=order_confirmationRepository.findById(id).get();
        orderConfirmation.setState(orderConfirmation.getState()+1);
        orderConfirmation.setUserValidator(get_connected());
        order_confirmationRepository.save(orderConfirmation);
        if(orderConfirmation.getState()==1)
        {
            return new RedirectView("/order/list");
        } else if (orderConfirmation.getState()==2) {
            return new RedirectView("/order/finance/list");
        }
        else
        {
            return new RedirectView("/order/DG/list");
        }

    }

    @GetMapping("order/refuse/{id}")
    public RedirectView refuse(Model model, @PathVariable int id)
    {
        Order_confirmation orderConfirmation=order_confirmationRepository.findById(id).get();
        if(orderConfirmation.getState()==0)
        {
            orderConfirmation.setState(-1);
        }
        else
        {
            orderConfirmation.setState(orderConfirmation.getState()*-1);
        }

        orderConfirmation.setUserValidator(get_connected());
        order_confirmationRepository.save(orderConfirmation);
        refuser(orderConfirmation);
        if(orderConfirmation.getState()==-1)
        {
            return new RedirectView("/order/list");
        } else if (orderConfirmation.getState()==-2) {
            return new RedirectView("/order/finance/list");
        }
        else
        {
            return new RedirectView("/order/DG/list");
        }
    }


    @GetMapping("order/details/{id}")
    public String details(Model model, @PathVariable int id)
    {
        Order_confirmation orderConfirmation=order_confirmationRepository.findById(id).get();
        model.addAttribute("orderConfirmation",orderConfirmation);
        return "order_confirmation/details";
    }
    @GetMapping("order/export/{id}")
    public ResponseEntity<byte[]> exportProforma(HttpServletResponse response, @PathVariable int id) throws DocumentException {
        Order_confirmation orderConfirmation=order_confirmationRepository.findById(id).get();

        // Create a new document
        Document document = new Document();

        // Create a new ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a new PdfWriter
        PdfWriter.getInstance(document,byteArrayOutputStream);

        // Open the document
        document.open();

        // Add a title to the document
        Paragraph title = new Paragraph("BON DE COMMANDE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25));
        document.add(title);


        document.add(new Paragraph("Fournisseur",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));
        PdfPTable table=new PdfPTable(2);
        table.addCell("Nom:");
        table.addCell(orderConfirmation.getSupplier().getName());
        table.addCell("Contact:");
        table.addCell(orderConfirmation.getSupplier().getContact());
        table.addCell("adresse:");
        table.addCell(orderConfirmation.getSupplier().getAdresse());
        table.addCell("Responsable");
        table.addCell(orderConfirmation.getSupplier().getResponsable());

        document.add(table);


        document.add(new Paragraph("DETAILS ACHAT ",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));

        PdfPTable table1=new PdfPTable(2);
        table1.addCell("Produit");table1.addCell(orderConfirmation.getProduct().getDesignation());
        table1.addCell("Quantite");table1.addCell(String.valueOf(orderConfirmation.getQuantity()));
        table1.addCell("Prix unitaire HT"); table1.addCell(String.valueOf(orderConfirmation.getPrice().getUnitPrice()));
        table1.addCell("TVA");table1.addCell(String.valueOf(orderConfirmation.getTva()));
        table1.addCell("Prix HT");table1.addCell(String.valueOf(orderConfirmation.getPrixHT()));
        table1.addCell("Prix TTC");table1.addCell(String.valueOf(orderConfirmation.getPrixTTC()));

        document.add(table1);

        document.close();

        // Convert the ByteArrayOutputStream to a byte array
        byte[] bytes = byteArrayOutputStream.toByteArray();

        // Create the HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.pdf");

        // Return the byte array as a ResponseEntity
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }




}
