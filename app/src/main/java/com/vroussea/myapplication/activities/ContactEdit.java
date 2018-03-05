package com.vroussea.myapplication.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactBuilder;
import com.vroussea.myapplication.contact.ContactDao;
import com.vroussea.myapplication.contact.ContactDatabase;

public class ContactEdit extends AppCompatActivity {

    ContactDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onSubmitContact(View view) {
        String firstName= ((EditText)findViewById(R.id.first_name)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.last_name)).getText().toString();
        String nickname = ((EditText)findViewById(R.id.nickname)).getText().toString();
        String phoneNumber = ((EditText)findViewById(R.id.phone_number)).getText().toString();
        String eMail = ((EditText)findViewById(R.id.eMail)).getText().toString();

        if (!phoneNumber.equals("") && !firstName.equals("")) {
            Contact newContact = ContactBuilder.aContact()
                    .withFirstName(firstName)
                    .withLastName(lastName)
                    .withNickname(nickname)
                    .withPhoneNumber(phoneNumber)
                    .withEMail(eMail).build();

            new Thread(() -> {
                mDao = ContactDatabase.getDatabase(getApplicationContext()).contactDao();
                mDao.insert(newContact);
            }).start();
        }
        finish();
    }
}
