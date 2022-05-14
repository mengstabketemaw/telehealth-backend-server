package io.telehelth.authorizationserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String type;

    @JsonIgnore
    @Lob
    private byte[] data;

    public String toString(){
        return name;
    }

}
