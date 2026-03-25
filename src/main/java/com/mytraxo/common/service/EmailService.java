package com.mytraxo.common.service;

//@Service ❌ keep disabled
public class EmailService {

    public void sendMail(String to, String subject, String text) {
        // ✅ do nothing (disabled)
        System.out.println("Email disabled: " + subject);
    }
}