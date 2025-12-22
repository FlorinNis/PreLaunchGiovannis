package com.brand.prelaunch.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Subscriber {

    @NotBlank(message = "INPUT_REQUIRED")
    private String name = "GUEST"; // Default to guest if not collected

    // Strict Regex to prevent malformed emails [cite: 105]
    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$", message = "INVALID_FORMAT")
    private String email;

    // THE HONEYPOT FIELD
    // This field should remain empty. If a bot fills it, we reject. [cite: 106, 171]
    private String websiteUrl;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
}