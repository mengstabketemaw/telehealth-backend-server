package io.telehelth.authorizationserver.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String firstname;
    String middlename;
    String lastname;
    @OneToOne(cascade = CascadeType.ALL)
    FileDB avatar;
    @Enumerated(EnumType.STRING)
    Roles Role;
    LocalDate birthDate;
    String sex;
    String phoneNumber;
    String homePhoneNumber;
    String email;
    String password;
    String latitude;
    String longitude;
  }
