package com.example.gestion_achat.controller;

import com.example.gestion_achat.entity.Price;
import com.example.gestion_achat.entity.Product;
import com.example.gestion_achat.entity.Supplier;
import com.example.gestion_achat.model.Proforma;
import com.example.gestion_achat.repository.PriceRepository;
import com.example.gestion_achat.repository.ProductRepository;
import com.example.gestion_achat.repository.SuppierRepository;
import com.example.gestion_achat.service.SupplierService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Setter
@Getter
@Controller
public class SupplierController {
    ProductRepository productRepository;
    SuppierRepository supplierRepository;

    PriceRepository priceRepository;


    public SupplierController(ProductRepository productRepository, SuppierRepository supplierRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.priceRepository = priceRepository;
    }

    @GetMapping("/purchase/details/supplier/export/{id_product}/{id_supplier}/{quantite}")
    public ResponseEntity<byte[]> exportProforma(HttpServletResponse response, @PathVariable int id_product,@PathVariable int id_supplier,@PathVariable int quantite) throws DocumentException {
        SupplierService supplierService=new SupplierService(supplierRepository,productRepository,priceRepository);
        Product product=productRepository.findById(id_product).get();
        Supplier supplier=supplierRepository.findById(id_supplier).get();
        Proforma proforma=supplierService.get_Proformat(supplier,product,quantite,20);


        // Create a new document
        Document document = new Document();

        // Create a new ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a new PdfWriter
        PdfWriter.getInstance(document,byteArrayOutputStream);

        // Open the document
        document.open();

        // Add a title to the document
        Paragraph title = new Paragraph("FACTURE PROFORMA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25));
        document.add(title);


        document.add(new Paragraph("Fournisseur",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));
        PdfPTable table=new PdfPTable(2);
        table.addCell("Nom:");
        table.addCell(proforma.getSupplier().getName());
        table.addCell("Contact:");
        table.addCell(proforma.getSupplier().getContact());
        table.addCell("adresse:");
        table.addCell(proforma.getSupplier().getAdresse());
        table.addCell("Responsable");
        table.addCell(proforma.getSupplier().getResponsable());

        document.add(table);


        document.add(new Paragraph("ACHAT ",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));

        PdfPTable table1=new PdfPTable(2);
        table1.addCell("Produit");table1.addCell(proforma.getProduct().getDesignation());
        table1.addCell("Quantite");table1.addCell(String.valueOf(proforma.getQuantity()));
        table1.addCell("Prix unitaire HT"); table1.addCell(String.valueOf(proforma.getPrice().getUnitPrice()));
        table1.addCell("TVA");table1.addCell(String.valueOf(proforma.getTVA()));
        table1.addCell("Prix HT");table1.addCell(String.valueOf(proforma.getPrixHT()));
        table1.addCell("Prix TTC");table1.addCell(String.valueOf(proforma.getPrixTTC()));

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
