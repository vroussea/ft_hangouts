package com.vroussea.myapplication.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactDao;
import com.vroussea.myapplication.contact.ContactDatabase;
import com.vroussea.myapplication.toasts.CustomToast;

public class MainActivity extends Activity {
    private ContactDao mUserDao;
    private ContactDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "contacts").build();
        mUserDao = mDb.contactDao();
    }

}
