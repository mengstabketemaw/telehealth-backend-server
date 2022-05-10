package io.telehelth.authorizationserver.configuration;

public class Constant {
    public static final String ENCODER_ID = "bcrypt";
    public static final String API_URL_PREFIX = "/api/**";
    public static final String H2_URL_PREFIX = "/h2-console/**";
    public static final String SIGNUP_URL = "/api/oauth/signup";
    public static final String TOKEN_URL = "/api/oauth/token";
    public static final String SING_OUT = "/api/oauth/signout";
    public static final String REFRESH_URL = "/api/auth/token/refresh";
    public static final String PRODUCTS_URL = "/api/oauth/calls";
    public static final String DOWNLOAD_URL = "/api/file/**";
    public static final String AUTHORIZATION =  "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000_000;
    public static final String ROLE_CLAIM = "roles";
    public static final String AUTHORITY_PREFIX = "ROLE_";
}
