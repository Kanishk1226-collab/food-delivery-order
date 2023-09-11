package com.example.food.delivery;

import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.DocumentException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;

@RestController
public class InvoiceController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    public BaseResponse<?> response;

    @GetMapping(path = "/invoice/pdf")
    public ResponseEntity<?> getPDF(@RequestParam int orderId) throws DocumentException {
        Order order = orderRepository.findById(orderId);
        if(order == null) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), "Order not found", null);
            return ResponseEntity.ok(response);
        }
        Context context = new Context();
        context.setVariable("orderEntry", order);
        String htmlContent = templateEngine.process("order-invoice-template", context);
        ConverterProperties converterProperties = new ConverterProperties();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String invoiceFileName = "order-" + orderId;
        headers.setContentDispositionFormData("attachment", invoiceFileName);

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

    }
}
