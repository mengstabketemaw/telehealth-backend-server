package io.telehelth.authorizationserver.model;
import java.util.List;

public class SignupResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String email;
    private String roles;

    public SignupResponse(String accessToken, String refreshToken,String email, String roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

