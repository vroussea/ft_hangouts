package com.vroussea.myapplication.contact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    void insertContact(Contact... contacts);

    @Update
    void updateContact(Contact... contacts);

    @Delete
    void deleteContact(Contact... contacts);

    @Query("SELECT * FROM user")
    LiteContact[] loadAllContacts();

    @Query("SELECT * FROM user WHERE first_name LIKE :search + '%'"
            + "OR last_name LIKE :search + '%'")
    List<LiteContact> findUserWithName(String search);
}
