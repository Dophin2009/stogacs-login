package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthTokenDto {

    private String token;

    @JsonProperty("expiration_date")
    private String expirationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

}
