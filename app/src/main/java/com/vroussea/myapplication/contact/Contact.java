package com.vroussea.myapplication.contact;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int  _id;

    @ColumnInfo(name = "first_name")
    public String firstName;
    public String lastName;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;
    public String eMail;
    public String nickname;

    @ColumnInfo(name = "profile_pic")
    Bitmap  profilePic;

}
