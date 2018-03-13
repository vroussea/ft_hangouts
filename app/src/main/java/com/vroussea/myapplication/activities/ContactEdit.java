package com.vroussea.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactBuilder;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.utils.Colors;

public class ContactEdit extends AppCompatActivity {
    ContactHelper contactHelper = new ContactHelper();

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        if (!getIntent().getBooleanExtra("isCreating", true)) {
            appendContactData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Colors colors = new Colors();

        getSupportActionBar().setBackgroundDrawable(colors.getActionBarColor());

        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, colors.getSatusBarColor()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onSubmitContact(View view) {
        if (getIntent().getBooleanExtra("isCreating", true)) {
            onCreateContact();
        } else {
            onEditContact();
        }
        finish();
    }

    private void onCreateContact() {
        String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phone_number)).getText().toString();
        String eMail = ((EditText) findViewById(R.id.eMail)).getText().toString();

        if (!phoneNumber.equals("") && !firstName.equals("")) {
            Contact newContact = ContactBuilder.aContact()
                    .withFirstName(firstName)
                    .withLastName(lastName)
                    .withNickname(nickname)
                    .withPhoneNumber(phoneNumber)
                    .withEMail(eMail).build();

            contactHelper.addContact(newContact);
        }
    }

    private void onEditContact() {
        String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phone_number)).getText().toString();
        String eMail = ((EditText) findViewById(R.id.eMail)).getText().toString();

        if (!phoneNumber.equals("") && !firstName.equals("")) {
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setNickname(nickname);
            contact.setPhoneNumber(phoneNumber);
            contact.setEMail(eMail);

            contactHelper.updateContact(contact);
        }
    }

    private void appendContactData() {
        contact = (Contact) getIntent().getSerializableExtra("contact");

        EditText firstName = ((EditText) findViewById(R.id.first_name));
        EditText lastName = findViewById(R.id.last_name);
        EditText nickname = findViewById(R.id.nickname);
        EditText phoneNumber = findViewById(R.id.phone_number);
        EditText eMail = findViewById(R.id.eMail);

        firstName.getText().clear();
        firstName.getText().append(contact.getFirstName());

        lastName.getText().clear();
        lastName.getText().append(contact.getLastName());

        nickname.getText().clear();
        nickname.getText().append(contact.getNickname());

        phoneNumber.getText().clear();
        phoneNumber.getText().append(contact.getPhoneNumber());

        eMail.getText().clear();
        eMail.getText().append(contact.getEMail());
    }
}
