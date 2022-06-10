package io.telehelth.authorizationserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String firstname;
    String middlename;
    String lastname;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    FileDB avatar;
    @Enumerated(EnumType.STRING)
    Roles Role;
    LocalDate birthDate;
    String sex;
    String phoneNumber;
    String homePhoneNumber;
    @Column(nullable = false,unique = true)
    String email;
    @JsonIgnore
    String password;
    String latitude;
    String longitude;
    boolean disabled;
  }
