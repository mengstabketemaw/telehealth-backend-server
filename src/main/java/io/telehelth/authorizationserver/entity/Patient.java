package io.telehelth.authorizationserver.entity;

import javax.persistence.*;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToOne
    User user;

    String martialStatus;


}
