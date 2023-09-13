package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.model.User;

import java.util.Date;

public interface EmailService {

    void sendSimpleMail(String subject, String body, String toEmail);

    void sendHtmlEmail(String name, String to);

    void sendLoginAlertEmail(User user, String browserInfo, Date loginDate);

    void sendOrderConfirmationEmail(Order order);

}
