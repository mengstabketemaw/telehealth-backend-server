package io.telehelth.authorizationserver.service;

import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {

    private final UserRepository userRepository;


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserModel userModel) throws IOException {
        User user = new User();
        BeanUtils.copyProperties(user,userModel,"avatar","specializationDocument");
        user.setAvatar(userModel.getAvatar().getBytes());
        user.setSpecializationDocument(userModel.getSpecializationDocument().getBytes());
        userRepository.save(user);
    }
}
