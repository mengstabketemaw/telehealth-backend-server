package io.telehelth.authorizationserver.service;

import io.telehelth.authorizationserver.entity.*;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.AdminRepository;
import io.telehelth.authorizationserver.repository.DoctorRepository;
import io.telehelth.authorizationserver.repository.PatientRepository;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class AuthService {

    private final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;



    public AuthService(UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    public void createUser(UserModel userModel) throws IOException {

        logger.info("creating ["+userModel.getRole().name()+"] account. received the following data");
        logger.info("UserModel ->"+userModel.toString());
        User user = new User();
        BeanUtils.copyProperties(userModel,user,"docRoles","martialStatus","avatar","specializationDocument");

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(userModel.getAvatar().getOriginalFilename()));
        FileDB avatar = new FileDB();
        avatar.setName(fileName);
        avatar.setData(userModel.getAvatar().getBytes());
        avatar.setType(userModel.getAvatar().getContentType());
        user.setAvatar(avatar);

        logger.info("User create Successfully");

        if(user.getRole().equals(Roles.DOCTOR)){
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setDocRoles(userModel.getDocRoles());
            FileDB doc = new FileDB();
            String filename = StringUtils.cleanPath(userModel.getAvatar().getOriginalFilename());
            doc.setName(fileName);
            doc.setType(userModel.getSpecializationDocument().getContentType());
            doc.setData(userModel.getSpecializationDocument().getBytes());
            doctor.setSpecializationDocument(doc);
            doctorRepository.save(doctor);
            logger.info("Doctor Account has been create ->"+doctor.toString());
        }
        else if (userModel.getRole().equals(Roles.PATIENT)){
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setMartialStatus(userModel.getMartialStatus());
            patientRepository.save(patient);
            logger.info("Patient Account has been create -> "+patient.toString());
        }else{
            Admin admin = new Admin();
            adminRepository.save(admin);
            logger.info("Doctor Account has been create");
        }

    }
}
