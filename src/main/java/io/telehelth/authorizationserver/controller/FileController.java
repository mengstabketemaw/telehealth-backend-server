package io.telehelth.authorizationserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.telehelth.authorizationserver.entity.Doctor;
import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import io.telehelth.authorizationserver.repository.PatientRepository;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class FileController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorREpository;
    private final PatientRepository patientRepository;

    public FileController(UserRepository userRepository, DoctorRepository doctorREpository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.doctorREpository = doctorREpository;
        this.patientRepository = patientRepository;
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
    public ResponseEntity<Map<String,Object>> getUserInformationById(@PathVariable long id){
        //returning the user information vary according to the user so, first we should know what kind of user this is.
        User user = userRepository.findById(id).get();
        Map<String,Object> response = new HashMap<>();
        response.put("name",user.getFirstname()+" "+user.getMiddlename()+" "+user.getLastname());
        response.put("sex",user.getSex());
        response.put("birthDate",user.getBirthDate());

        return null;
    }


}
