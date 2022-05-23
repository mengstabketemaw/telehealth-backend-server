package io.telehelth.authorizationserver.controller;

import io.telehelth.authorizationserver.entity.Doctor;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorRepository doctorRepository;

    public AdminController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors(){
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable long id){
        return ResponseEntity.ok(doctorRepository.getById(id));
    }

}
