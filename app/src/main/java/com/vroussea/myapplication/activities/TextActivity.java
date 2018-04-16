package com.vroussea.myapplication.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.utils.Colors;
import com.vroussea.myapplication.utils.PhoneNumberPrefix;

import java.util.ArrayList;

public class TextActivity extends AppCompatActivity {

    private final ContactHelper contactHelper = new ContactHelper();

    private ArrayList<String> smsMessagesList = new ArrayList<>();
    private ListView messages;
    private EditText input;
    private ArrayAdapter<String> arrayAdapter;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        try {
            contact = contactHelper.getContactById((int) getIntent().getSerializableExtra("contactId"));
        } catch (Exception e) {
            Log.d("errorDisplayContact", e.getMessage());
        }

        messages = findViewById(R.id.messages);
        input = findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);
        getPermissionToReadSMS();
        messages.setSelection(arrayAdapter.getCount() - 1);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        } else {
            refreshSmsInbox();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                refreshSmsInbox();
            } else {
                Toast.makeText(this, R.string.permissionsNotGranted, Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshSmsInbox() {
        String[] address = {PhoneNumberPrefix.addPrefix(contact.getPhoneNumber())};

        ContentResolver contentResolver = getContentResolver();
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms"), null, "address = ?", address, "date");
        int indexBody = smsCursor.getColumnIndex("body");
        int indexAddress = smsCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsCursor.getString(indexAddress) +
                    "\n" + smsCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsCursor.moveToNext());
    }
}
