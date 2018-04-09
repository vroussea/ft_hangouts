package com.vroussea.myapplication.contact;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

@Entity(tableName = "contacts")
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int  _id;

    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    private String eMail;
    private String nickname;

    private Bitmap picture;

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

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap profilePic) {
        this.picture = profilePic;
    }
}
