package io.telehelth.authorizationserver.model;

import lombok.Data;

@Data
public class SignedInUser {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String userId;
    private String role;

}
