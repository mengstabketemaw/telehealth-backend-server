package io.telehelth.authorizationserver.controller;


import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.model.Dummy;
import io.telehelth.authorizationserver.model.SignedInUser;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.UserRepository;
import io.telehelth.authorizationserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @PostMapping("/signup")
    public ResponseEntity<SignedInUser> createUser(@Valid @ModelAttribute UserModel userModel) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.createUser(userModel));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<SignedInUser> getAccessToken(@RequestBody Map<String,String> body){
        return ResponseEntity.ok(authService.getAccessToken(body.get("refreshToken")).get());
    }

    @PostMapping("/token")
    public ResponseEntity<SignedInUser> signIn(@RequestBody Map<String,String> body){
        String username = body.get("username");
        String password = body.get("password");
        User user = userRepository.findUserByEmail(username).orElseThrow(()->new UsernameNotFoundException("Username not Found: "+username));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok(authService.getSignedInUser(user));
        }
        throw new InsufficientAuthenticationException("Unauthorized.");
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(@RequestBody Map<String,String> body) {
        // We are using removeToken API for signout.
        // Ideally you would like to get tgit she user ID from Logged in user's request
        // and remove the refresh token based on retrieved user id from request.
        String refreshToken = body.get("refreshToken");
        authService.removeRefreshToken(refreshToken);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Worked");
    }

}
