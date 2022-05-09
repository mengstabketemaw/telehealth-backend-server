package io.telehelth.authorizationserver.entity;

import javax.persistence.*;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToOne
    User user;

    String[] docRoles;

}
