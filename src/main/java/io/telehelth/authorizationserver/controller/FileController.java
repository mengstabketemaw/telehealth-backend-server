package io.telehelth.authorizationserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.telehelth.authorizationserver.entity.*;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.AdminRepository;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import io.telehelth.authorizationserver.repository.PatientRepository;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class FileController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorREpository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public FileController(UserRepository userRepository, DoctorRepository doctorREpository, PatientRepository patientRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorREpository = doctorREpository;
        this.patientRepository = patientRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/avatar/{username}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String username){
        User user = userRepository.findUserByEmail(username).orElseThrow();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,user.getAvatar().getType())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+user.getAvatar().getName()+"\"")
                .body(user.getAvatar().getData());
    }

    @GetMapping("/doctor/document/{id}")
    public ResponseEntity<byte[]> getSpecializationDocument(@PathVariable long id){
        Doctor doctor = doctorREpository.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,doctor.getSpecializationDocument().getType())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+doctor.getSpecializationDocument().getName()+"\"")
                .body(doctor.getSpecializationDocument().getData());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUserInformationById(@PathVariable long id){
        //returning the user information vary according to the user so, first we should know what kind of user this is.
        User user = userRepository.findById(id).get();
        if(user.getRole().equals(Roles.PATIENT)){
            Patient patient = patientRepository.findByUserId(user.getId()).get();
            return ResponseEntity.ok(patient);
        }else if(user.getRole().equals(Roles.DOCTOR)){
            Doctor doctor = doctorREpository.findByUser(user).get();
            return ResponseEntity.ok(doctor);
        }
            return ResponseEntity.ok(adminRepository.findByUser(user).get());
    }
    //this method is deprecated by MENGSTABKETEMAW
    @GetMapping("/username/{email}")
    public ResponseEntity<Object> getUserInformationByEmail(@PathVariable String email){
        //returning the user information vary according to the user so, first we should know what kind of user this is.
        User user = userRepository.findUserByEmail(email).get();
        if(user.getRole().equals(Roles.PATIENT)){
            Patient patient = patientRepository.findByUserId(user.getId()).get();
            return ResponseEntity.ok(patient);
        }else if(user.getRole().equals(Roles.DOCTOR)){
            Doctor doctor = doctorREpository.findByUser(user).get();
            return ResponseEntity.ok(doctor);
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/")
    public ResponseEntity<User> updateUserInformation(@RequestBody @Valid UserModel user,Authentication authentication) throws IOException {
        User oldUser = userRepository.findUserByEmail(authentication.getName()).get();
        BeanUtils.copyProperties(user,oldUser,"id","avatar","role","password");
        //update the password if given and the password is more than 5 char
        if(user.getPassword()!=null){
            if(user.getPassword().length()>4){
                oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return ResponseEntity.ok(userRepository.save(oldUser));
    }

    @PutMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@RequestPart MultipartFile file, Authentication authentication) throws IOException {
        User user = userRepository.findUserByEmail(authentication.getName()).get();
        var ava = user.getAvatar();
        ava.setName(file.getName());
        ava.setType(file.getContentType());
        ava.setData(file.getBytes());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
