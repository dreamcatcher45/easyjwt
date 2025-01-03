package com.github.dreamcatcher45.easyjwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @NotBlank(message = "JWT secret is required")
    private String secret;

    @Min(value = 1, message = "Expiration hours must be at least 1")
    private int expirationHours = 24;

    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";

    // Getters and Setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    
    public int getExpirationHours() { return expirationHours; }
    public void setExpirationHours(int expirationHours) { this.expirationHours = expirationHours; }
    
    public String getTokenPrefix() { return tokenPrefix; }
    public void setTokenPrefix(String tokenPrefix) { this.tokenPrefix = tokenPrefix; }
    
    public String getHeaderName() { return headerName; }
    public void setHeaderName(String headerName) { this.headerName = headerName; }
}