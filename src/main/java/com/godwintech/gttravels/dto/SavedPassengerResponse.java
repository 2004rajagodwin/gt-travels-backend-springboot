package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.IdDocumentType;
import com.godwintech.gttravels.enums.PassengerGender;

public class SavedPassengerResponse {

    private Long id;
    private String name;
    private Integer age;
    private PassengerGender gender;
    private String mobile;
    private String email;
    private IdDocumentType idType;
    private String idNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public PassengerGender getGender() {
        return gender;
    }

    public void setGender(PassengerGender gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IdDocumentType getIdType() {
        return idType;
    }

    public void setIdType(IdDocumentType idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
