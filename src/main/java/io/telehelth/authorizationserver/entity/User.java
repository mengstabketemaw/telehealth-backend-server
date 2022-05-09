package io.telehelth.authorizationserver.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String firstname;

    String middlename;

    String lastname;

    @Lob
    byte[] avatar;

    @Enumerated(EnumType.STRING)
    Roles Role;

    LocalDate birthDate;

    String sex;

    String phoneNumber;

    String homePhoneNumber;

    String password;

    String martialStatus;

    String[] docRoles;

    String latitude;

    String longitude;

    @Lob
    byte[] specializationDocument;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Roles getRole() {
        return Role;
    }

    public void setRole(Roles role) {
        Role = role;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String[] getDocRoles() {
        return docRoles;
    }

    public void setDocRoles(String[] docRoles) {
        this.docRoles = docRoles;
    }

    public byte[] getSpecializationDocument() {
        return specializationDocument;
    }

    public void setSpecializationDocument(byte[] specializationDocument) {
        this.specializationDocument = specializationDocument;
    }
}
