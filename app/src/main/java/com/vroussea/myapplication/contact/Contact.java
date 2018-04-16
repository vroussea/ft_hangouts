package com.vroussea.myapplication.contact;

import java.io.Serializable;

public class Contact implements Serializable {
    private int _id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String eMail;
    private String nickname;
    private byte[] picture;

    public Contact() {
        picture = null;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] profilePic) {
        this.picture = profilePic;
    }
}
