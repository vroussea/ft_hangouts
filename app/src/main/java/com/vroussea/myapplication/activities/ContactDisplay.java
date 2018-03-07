package com.vroussea.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;

public class ContactDisplay extends AppCompatActivity {

    ContactHelper contactHelper = new ContactHelper();

    Contact currentContact;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_contact) {
            Intent intent = new Intent(this, ContactEdit.class);
            intent.putExtra("isCreating", false);
            intent.putExtra("contact", currentContact);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_delete_contact) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_contact_display);

        try {
            currentContact = contactHelper.getContactById((int) getIntent().getSerializableExtra("contact"));
        } catch (Exception e) {

        }

        displayContactData();
    }

    private void displayContactData() {
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
