package com.vroussea.myapplication.activities;

import android.os.Bundle;
import android.app.Activity;

import com.vroussea.myapplication.R;

public class ContactEdit extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
