package io.telehelth.authorizationserver.service;

import io.telehelth.authorizationserver.configuration.JwtManager;
import io.telehelth.authorizationserver.entity.*;
import io.telehelth.authorizationserver.model.SignedInUser;
import io.telehelth.authorizationserver.model.UserModel;
import io.telehelth.authorizationserver.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class AuthService {

    private final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final UserRepository userRepository;
    private final JwtManager jwtManager;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository userTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;



    public AuthService(UserRepository userRepository,
                       JwtManager jwtManager,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository,
                       AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenRepository userTokenRepository, RefreshTokenRepository refreshTokenRepository) {

        this.userRepository = userRepository;
        this.jwtManager = jwtManager;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.userTokenRepository = userTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public SignedInUser createUser(UserModel userModel) throws IOException {

        logger.info("creating ["+userModel.getRole().name()+"] account. received the following data");
        logger.info("UserModel ->"+userModel.toString());
        User user = new User();
        BeanUtils.copyProperties(userModel,user,"docRoles","martialStatus","avatar","specializationDocument");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(userModel.getAvatar().getOriginalFilename()));
        FileDB avatar = new FileDB();
        avatar.setName(fileName);
        avatar.setData(userModel.getAvatar().getBytes());
        avatar.setType(userModel.getAvatar().getContentType());
        user.setAvatar(avatar);

        logger.info("User create Successfully");

        if(user.getRole().equals(Roles.DOCTOR)){
            Doctor doctor = new Doctor();
            user.setDisabled(true);// doctors account must be enabled by admin before they are being used;
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
            admin.setUser(user);
            adminRepository.save(admin);
            logger.info("Doctor Account has been create");
        }
        return createSignedUserWithRefreshToken(user);
    }

    private SignedInUser createSignedUserWithRefreshToken(User user) {
        var signedInUser = createSignedInUser(user);
        signedInUser.setRefreshToken(createRefreshToken(user));
        return signedInUser;
    }

    private SignedInUser createSignedInUser(User user) {
        String token = jwtManager.create(org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build());
        SignedInUser signedInUser = new SignedInUser();
        signedInUser.setUserId(user.getId()+"");
        signedInUser.setRole(user.getRole().name());
        signedInUser.setAccessToken(token);
        signedInUser.setUsername(user.getEmail());
        return signedInUser;
    }

    private String createRefreshToken(User user) {
        String token = RandomHolder.randomKey(128);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public SignedInUser getSignedInUser(User user) {
        userTokenRepository.deleteByUser(user);
        return createSignedUserWithRefreshToken(user);
    }

    public Optional<SignedInUser> getAccessToken(String refreshToken) {
        // You may add a validation for time that would remove/invalidate the refresh token
        return Optional.ofNullable(userTokenRepository.findByToken(refreshToken)
                .map(rt -> {
                    var tempo = createSignedInUser(rt.getUser());
                    tempo.setRefreshToken(refreshToken);
                    return tempo;
                })
                .orElseThrow(() -> new RuntimeException("Invalid token.")));
    }

    public void removeRefreshToken(String token) {
        userTokenRepository.findByToken(token)
                .ifPresentOrElse(userTokenRepository::delete, () -> {
                    throw new RuntimeException("Invalid token.");
                });
    }

    // https://stackoverflow.com/a/31214709/109354
    // or can use org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(n)
    private static class RandomHolder {
        static final Random random = new SecureRandom();
        public static String randomKey(int length) {
            return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
                    .toString(32)).replace('\u0020', '0');
        }
    }
}
