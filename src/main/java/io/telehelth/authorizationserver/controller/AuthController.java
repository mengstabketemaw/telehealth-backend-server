package io.telehelth.authorizationserver.controller;


import io.telehelth.authorizationserver.entity.RefreshToken;
import io.telehelth.authorizationserver.exceptions.TokenRefreshException;
import io.telehelth.authorizationserver.model.Dummy;
import io.telehelth.authorizationserver.model.TokenRefreshResponse;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.service.AuthService;
import io.telehelth.authorizationserver.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@Valid @ModelAttribute UserModel userModel) throws IOException {
        authService.createUser(userModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/test",consumes = "multipart/form-data",method = RequestMethod.POST)
    public ResponseEntity<String> test(@Valid @ModelAttribute Dummy date){
        System.out.println(date.getDate().toString());
        return ResponseEntity.ok("Worked");
    }

}
