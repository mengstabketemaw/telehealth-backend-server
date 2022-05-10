package io.telehelth.authorizationserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @OneToOne(cascade = CascadeType.ALL)
    User user;

    String martialStatus;

}
