package com.vroussea.myapplication;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.vroussea.myapplication.contact.ContactDatabase;

public class App extends Application {
    private static App myContext;

    public static App getContext() {
        return myContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myContext = this;

        ContactDatabase mDb = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "contacts").build();
    }
}