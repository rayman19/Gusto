package com.yesipov.gusto.Models;

import com.google.type.Date;

public class User {
    private String name;
    private String email;
    private String pass;
    private String phone;
    private String surname;
    private String birthday;
    private Integer points;

    public User() {}

    public User(String name, String email, String pass, String phone, String surname, String birthday) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.surname = surname;
        this.birthday = birthday;
        this.points = 100;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }
}
