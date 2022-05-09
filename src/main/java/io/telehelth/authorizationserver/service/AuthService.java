package io.telehelth.authorizationserver.service;

import io.telehelth.authorizationserver.entity.Roles;
import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class AuthService {

    private Logger logger = Logger.getLogger(AuthService.class.getName());
    private final UserRepository userRepository;



    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserModel userModel) throws IOException {

        User user = new User();
        logger.info("creating - ["+userModel.getRole().toString()+"] - account :-"+userModel.toString());
        BeanUtils.copyProperties(userModel,user,"docRoles","avatar","specializationDocument");
        user.setRole(userModel.getRole());
        user.setAvatar(userModel.getAvatar().getBytes());
        if(user.getRole().equals(Roles.DOCTOR)) {
            user.setSpecializationDocument(userModel.getSpecializationDocument().getBytes());
            user.setDocRoles(userModel.getDocRoles());
        }
        userRepository.save(user);
    }
}
