package com.vroussea.myapplication.contact;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

public class LiteContact {
    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "profile_pic")
    Bitmap  profilePic;
}
