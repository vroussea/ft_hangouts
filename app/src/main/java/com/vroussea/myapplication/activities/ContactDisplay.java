package com.vroussea.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactDao;

public class ContactDisplay extends AppCompatActivity {

    ContactDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        displayContactData((Contact) getIntent().getSerializableExtra("contact"));
    }

    private void displayContactData(Contact currentContact) {
        TextView firstName = findViewById(R.id.first_name);
        TextView lastName = findViewById(R.id.last_name);
        TextView nickname = findViewById(R.id.nickname);
        TextView phoneNumber = findViewById(R.id.phone_number);
        TextView eMail = findViewById(R.id.eMail);

        firstName.setText(currentContact.getFirstName());
        lastName.setText(currentContact.getLastName());
        nickname.setText(currentContact.getNickname());
        phoneNumber.setText(currentContact.getPhoneNumber());
        eMail.setText(currentContact.getEMail());
    }
}
