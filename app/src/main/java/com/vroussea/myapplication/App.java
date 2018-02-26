package com.vroussea.myapplication;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Bundle;

import com.vroussea.myapplication.contact.ContactDao;
import com.vroussea.myapplication.contact.ContactDatabase;

public class App extends Application {
    private ContactDao mUserDao;
    private ContactDatabase mDb;

    @Override
    public void onCreate() {
        super.onCreate();

        mDb = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "contacts").build();
    }
}