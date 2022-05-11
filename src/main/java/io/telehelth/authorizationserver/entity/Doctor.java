package io.telehelth.authorizationserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToOne(cascade = CascadeType.ALL)
    User user;

    String[] docRoles;

    String specialization;

    @OneToOne(cascade = CascadeType.ALL)
    FileDB specializationDocument;

}
