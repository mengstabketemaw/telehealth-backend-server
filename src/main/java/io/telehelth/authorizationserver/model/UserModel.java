package io.telehelth.authorizationserver.model;

import io.telehelth.authorizationserver.entity.DocRoles;
import io.telehelth.authorizationserver.entity.Roles;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserModel {

    @NotNull
    String firstname;
    @NotNull
    String middlename;
    @NotNull
    String lastname;
    @NotNull
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
    String password;
    @NotNull
    String longitude;
    @NotNull
    String latitude;

    //this properties may be null considering who is registering
    MultipartFile avatar;

    MultipartFile specializationDocument;

    String martialStatus;

    String[] docRoles;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
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

    public MultipartFile getSpecializationDocument() {
        return specializationDocument;
    }

    public void setSpecializationDocument(MultipartFile specializationDocument) {
        this.specializationDocument = specializationDocument;
    }
    public String toString(){
        return "["+firstname+"-"+middlename+"-"+lastname+"-"+sex+"-"+role.toString()+"]";
    }
}
