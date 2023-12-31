package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.controller.admin.AdminOrderController;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.User;
import com.nhn.cigarwebapp.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String FROM_NAME = "Cigar For Boss";

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${settings.cors_origin}")
    private String frontEndEndpoint;

    @Override
    @Async
    public void sendLoginAlertEmail(User user, String browserInfo, Date loginDate) {
        try {
            Context context = new Context();
            context.setVariable("linkToHomePage", frontEndEndpoint);
            context.setVariable("username", user.getUsername());
            context.setVariable("fullName", user.getFirstName() + " " + user.getLastName());
            context.setVariable("loginDate", loginDate);
            context.setVariable("browserInfo", browserInfo);
            String text = templateEngine.process("LoginAlert", context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CHARACTER_ENCODING);
            helper.setPriority(1);
            helper.setSubject("Có phiên đăng nhập vào tài khoản Cigar For Boss");
            helper.setFrom(fromEmail, FROM_NAME);
            helper.setTo(user.getEmail());
            helper.setText(text, true);

            emailSender.send(message);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendOrderConfirmationEmail(Order order) {
        try {
            Context context = new Context();
            context.setVariable("linkToHomePage", frontEndEndpoint);
            context.setVariable("customerFullName", order.getCustomer().getFullName());
            context.setVariable("orderId", order.getId());
            context.setVariable("linkToTrackingOrder",
                    String.format("%s/tracking-order", frontEndEndpoint));
            String text = templateEngine.process("OrderConfirmation", context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CHARACTER_ENCODING);
            helper.setPriority(3);
            helper.setSubject("Cigar For Boss đã nhận đơn hàng #" + order.getId());
            helper.setFrom(fromEmail, FROM_NAME);
            helper.setTo(order.getCustomer().getEmail());
            helper.setText(text, true);

            emailSender.send(message);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

}
