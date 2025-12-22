package com.brand.prelaunch.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void save(String email) {
        // In production, save to PostgreSQL or trigger Klaviyo API here.
        System.out.println("NEW_SUBSCRIBER_SECURED: " + email);
    }
}