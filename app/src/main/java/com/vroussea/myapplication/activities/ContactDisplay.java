package com.vroussea.myapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.utils.BitmapToBytes;
import com.vroussea.myapplication.utils.Colors;

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
    public void onResume() {
        super.onResume();

        Colors colors = new Colors();

        getSupportActionBar().setBackgroundDrawable(colors.getActionBarColor());

        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, colors.getSatusBarColor()));

        displayContactData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_sms_contact:
                intent = new Intent(this, TextActivity.class);
                intent.putExtra("contactId", currentContact.get_id());
                startActivity(intent);
                return true;
            case R.id.action_edit_contact:
                intent = new Intent(this, ContactEdit.class);
                intent.putExtra("isCreating", false);
                intent.putExtra("contactId", currentContact.get_id());
                new Thread(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }).start();
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_delete_contact:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.delete_alert)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                contactHelper.removeContact(currentContact);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);
    }

    private void displayContactData() {
        try {
            currentContact = contactHelper.getContactById((int) getIntent().getSerializableExtra("contact"));
        } catch (Exception e) {
            Log.d("errorDisplayContact", e.getMessage());
        }

        ImageView picture = findViewById(R.id.picture);

        if (currentContact.getPicture() != null)
            picture.setImageBitmap(BitmapToBytes.getImage(currentContact.getPicture()));

        TextView first_name = getTextView(R.id.first_name, R.string.first_name);
        TextView last_name = getTextView(R.id.last_name, R.string.last_name);
        TextView nickname = getTextView(R.id.nickname, R.string.nickname);
        TextView phone_number = getTextView(R.id.phone_number, R.string.phone_number);
        TextView eMail = getTextView(R.id.eMail, R.string.eMail);

        append(first_name, currentContact.getFirstName());
        append(last_name, currentContact.getLastName());
        append(nickname, currentContact.getNickname());
        append(phone_number, currentContact.getPhoneNumber());
        append(eMail, currentContact.getEMail());
    }

    private TextView getTextView(int textId, int stringId) {
        TextView textView = findViewById(textId);
        textView.setText(getString(stringId));
        return textView;
    }

    private void append(TextView textView, String text) {
        if (text.isEmpty()) {
            text = getResources().getString(R.string.empty_field);
        }
        textView.append(" : " + text);
    }
}
