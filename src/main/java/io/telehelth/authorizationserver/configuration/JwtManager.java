package io.telehelth.authorizationserver.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtManager {

    private  final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtManager(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String create(UserDetails principal){
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("AASTU Research Management Center of Excellence")
                .withSubject(principal.getUsername())
                .withClaim("roles",principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now+ Constant.EXPIRATION_TIME))
                .sign(Algorithm.RSA256(publicKey,privateKey));
    }



}
