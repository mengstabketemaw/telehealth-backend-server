package io.telehelth.authorizationserver.controller;


import io.telehelth.authorizationserver.model.Dummy;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/oauth/signup")
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
