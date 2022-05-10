package io.telehelth.authorizationserver.controller;

import io.telehelth.authorizationserver.entity.Doctor;
import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/file")
public class FileController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorREpository;

    public FileController(UserRepository userRepository, DoctorRepository doctorREpository) {
        this.userRepository = userRepository;
        this.doctorREpository = doctorREpository;
    }

    @GetMapping("/avatar/{username}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String username){
        User user = userRepository.findUserByEmail(username).orElseThrow();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,user.getAvatar().getType())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+user.getAvatar().getName()+"\"")
                .body(user.getAvatar().getData());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<byte[]> getSpecializationDocument(@PathVariable long id){
        Doctor doctor = doctorREpository.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,doctor.getSpecializationDocument().getType())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+doctor.getSpecializationDocument().getName()+"\"")
                .body(doctor.getSpecializationDocument().getData());
    }


}
