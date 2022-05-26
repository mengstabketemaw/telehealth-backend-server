package io.telehelth.authorizationserver.controller;

import io.telehelth.authorizationserver.entity.*;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.AdminRepository;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import io.telehelth.authorizationserver.repository.PatientRepository;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    public AdminController(DoctorRepository doctorRepository, UserRepository userRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors(){
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(){
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> activateAccount(@RequestParam boolean disabled, @PathVariable long id){
        User user = userRepository.getById(id);
        user.setDisabled(disabled);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<byte[]> getDoctorsDocument(@PathVariable long id){
        Doctor doctor = doctorRepository.findById(id).get();
        var doc = doctor.getSpecializationDocument();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,doc.getType())
                .header("file_name",doc.getName())
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+doc.getName()+"\"")
                .body(doc.getData());
    }

    @PostMapping("/")
    public ResponseEntity<Void> addAdmin(@ModelAttribute @Valid UserModel userModel) throws IOException {
        User user = new User();
        BeanUtils.copyProperties(userModel,user,"avatar","Role","password","latitude","longitude");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        FileDB fileDB = new FileDB();
        fileDB.setType(userModel.getAvatar().getContentType());
        fileDB.setData(userModel.getAvatar().getBytes());
        fileDB.setName(userModel.getAvatar().getName());
        user.setAvatar(fileDB);
        Admin newAdmin = new Admin();
        newAdmin.setUser(user);
        adminRepository.save(newAdmin);
        return ResponseEntity.ok().build();
    }

}
