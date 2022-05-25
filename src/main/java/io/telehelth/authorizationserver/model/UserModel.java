package io.telehelth.authorizationserver.model;

import io.telehelth.authorizationserver.entity.Roles;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UserModel {

    @NotNull
    String firstname;
    @NotNull
    String middlename;
    @NotNull
    String lastname;
    Roles role;
    @NotNull
    LocalDate birthDate;
    @NotNull
    String sex;
    @NotNull
    String phoneNumber;
    @NotNull
    String homePhoneNumber;
    @NotNull
    String email;
    String password;
    @NotNull
    String longitude;
    @NotNull
    String latitude;

    //this properties may be null considering who is registering
    MultipartFile avatar;

    MultipartFile specializationDocument;

    String martialStatus;

    String specialization;

    String[] docRoles;

}
