package io.telehelth.authorizationserver.controller;

import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {

    private final UserRepository userRepository;


    public FileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/avatar/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id){
        User user = userRepository.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"avatar\"")
                .body(user.getAvatar());
    }


}
