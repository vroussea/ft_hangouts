package com.vroussea.myapplication.contact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Contact... contacts);

    @Update
    void update(Contact... contacts);

    @Delete
    void delete(Contact... contacts);

    @Query("SELECT * FROM contacts")
    List<Contact> loadAllContacts();

    @Query("SELECT * FROM contacts WHERE first_name LIKE :search"
            + " OR last_name LIKE :search")
    List<Contact> findContactsByName(String search);

    @Query("SELECT * FROM contacts WHERE _id LIKE :search")
    Contact findContactById(int search);
}
