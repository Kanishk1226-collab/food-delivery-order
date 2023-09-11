package com.example.food.delivery;

import com.example.food.delivery.Response.BaseResponse;
import com.example.food.delivery.Response.ResponseStatus;
import com.example.food.delivery.ServiceInterface.DeliveryService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Random;

@Service
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    public BaseResponse<?> response;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    private ServletContext servletContext;

    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), "Error Sending E-Mail", null);
        }
    }

    public String generateOtp() {
        int otpLength = 4;

        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public synchronized ResponseEntity<BaseResponse<?>> sendOtp(int deliveryId, String delAgentEmail) {
        try {
            Delivery delivery = deliveryRepository.findById(deliveryId);
            if(!delivery.getDeliveryPersonId().equals(delAgentEmail)) {
                throw new OrderManagementExceptions.DeliveryNotFound("Delivery not found for email id " + delAgentEmail);
            }
            if(delivery.getIsDelivered()) {
                throw new OrderManagementExceptions.InvalidInputException("Order already delivered!");
            }
            String otp = generateOtp();
            Order order = orderRepository.findById(delivery.getOrderId());
            delivery.setOtp(otp);
            deliveryRepository.save(delivery);
            String subject = "Your One-Time Password for Delivery Confirmation";
            Context context = new Context();
            context.setVariable("otp", otp);
            sendEmailWithHtmlTemplate(order.getCartId(), subject, "otp-email-template", context);
            response = new BaseResponse<>(true, ResponseStatus.SUCCESS.getStatus(), null, "OTP Sent Successfully");
        } catch (Exception e) {
            response = new BaseResponse<>(false, ResponseStatus.ERROR.getStatus(), e.getMessage(), null);
        }
        return ResponseEntity.ok(response);
    }

}
