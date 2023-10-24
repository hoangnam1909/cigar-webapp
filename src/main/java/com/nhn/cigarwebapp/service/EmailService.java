package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.User;

import java.util.Date;

public interface EmailService {

    void sendLoginAlertEmail(User user, String browserInfo, Date loginDate);

    void sendOrderConfirmationEmail(Order order);

}
