package com.vroussea.myapplication.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.adapters.MessageAdapter;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.message.Message;
import com.vroussea.myapplication.message.MessageBuilder;
import com.vroussea.myapplication.utils.Colors;
import com.vroussea.myapplication.utils.PhoneNumberPrefix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TextActivity extends AppCompatActivity {

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 2;
    private static final int RECEIVE_SMS_PERMISSIONS_REQUEST = 3;

    private final static String emptyRegex = ".{0}";
    private final ContactHelper contactHelper = new ContactHelper();
    private ListView messages;
    private EditText input;
    private MessageAdapter messageAdapter;
    private Contact contact;

    private BroadcastReceiver SMSReceiver;

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
        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        messages.setAdapter(messageAdapter);
        getPermissionToReadSMS();

        getPermissionToReceiveSMS();

        SMSReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshSmsInbox();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        Colors colors = new Colors();

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(10000);

        registerReceiver(SMSReceiver, filter);

        getSupportActionBar().setBackgroundDrawable(colors.getActionBarColor());

        Window window = getWindow();

        refreshSmsInbox();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, colors.getSatusBarColor()));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(SMSReceiver);
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

    public void getPermissionToReceiveSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                    RECEIVE_SMS_PERMISSIONS_REQUEST);
        }
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

    public void tryToSendSMS(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSIONS_REQUEST);
        } else {
            sendSMS();
        }
    }

    private void sendSMS() {
        String message = input.getText().toString();
        if (!message.matches(emptyRegex)) {
            try {
                String contactNumber = contact.getPhoneNumber();
                SmsManager.getDefault().sendTextMessage(contactNumber, null,
                        message, null, null);

                Long timeLong = System.currentTimeMillis() / 1000;
                String timeStamp = timeLong.toString();

                input.getText().clear();
                messageAdapter.add(MessageBuilder.aMessage()
                        .withMe(true)
                        .withSenderName(contactNumber)
                        .withText(message)
                        .withTime(getDate(timeStamp)).build());
                messageAdapter.notifyDataSetChanged();
                messages.setSelection(messageAdapter.getCount() - 1);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
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

        } else if (requestCode == SEND_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                refreshSmsInbox();
            } else {
                Toast.makeText(this, R.string.permissionsNotGranted, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == RECEIVE_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                refreshSmsInbox();
            } else {
                getPermissionToReceiveSMS();
                Toast.makeText(this, R.string.permissionsNotGranted, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void refreshSmsInbox() {
        messageAdapter.clear();
        final int MYTYPE = 2;
        String[] address = {contact.getPhoneNumber(), PhoneNumberPrefix.addPrefix(contact.getPhoneNumber())};

        ContentResolver contentResolver = getContentResolver();

        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms"), null, "address = ? or address = ?", address, "date");
        int indexBody = smsCursor.getColumnIndex("body");
        int indexIsMe = smsCursor.getColumnIndex("type");
        int indexDate = smsCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsCursor.moveToFirst()) return;

        do {
            boolean isMe = smsCursor.getInt(indexIsMe) == MYTYPE;
            Message message = MessageBuilder.aMessage()
                    .withSenderName(contact.getFirstName())
                    .withText(smsCursor.getString(indexBody))
                    .withMe(isMe)
                    .withTime(getDate(smsCursor.getString(indexDate))).build();
            messageAdapter.add(message);

        } while (smsCursor.moveToNext());
        messageAdapter.notifyDataSetChanged();
        messages.setSelection(messageAdapter.getCount() - 1);
    }

    private String getDate(String timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(Long.parseLong(timeStamp)));
    }
}
