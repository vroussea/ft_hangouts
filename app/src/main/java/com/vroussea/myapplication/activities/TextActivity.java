package com.vroussea.myapplication.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.R;
import com.vroussea.myapplication.adapters.MessageAdapter;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.message.Message;
import com.vroussea.myapplication.message.MessageBuilder;
import com.vroussea.myapplication.utils.Colors;
import com.vroussea.myapplication.utils.PhoneNumberPrefix;

import java.util.ArrayList;

import static android.provider.Telephony.TextBasedSmsColumns.ADDRESS;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;
import static android.provider.Telephony.TextBasedSmsColumns.READ;
import static android.provider.Telephony.TextBasedSmsColumns.TYPE;

public class TextActivity extends AppCompatActivity {

    private final ContactHelper contactHelper = new ContactHelper();

    private ArrayList<String> smsMessagesList = new ArrayList<>();
    private ListView messages;
    private EditText input;
    private MessageAdapter messageAdapter;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 2;
    private static final String SENT_SMS_FLAG = "sending sms flag";
    private final static String emptyRegex = ".{0}";
    private Contact contact;

    private final int METYPE = 2;

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
        messages.setSelection(messageAdapter.getCount() - 1);

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

    public void tryToSendSMS(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSIONS_REQUEST);
        }
        else {
            sendSMS();
        }
    }

    private void sendSMS() {
        String message = input.getText().toString();
        if (!message.matches(emptyRegex)) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                Intent intent = new Intent(SENT_SMS_FLAG);
                Bundle values = new Bundle();
                values.putString("body", message);
                values.putString("address", contact.getPhoneNumber());

                intent.putExtra("sms", values);

                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0,
                        intent, 0);

                smsManager.sendTextMessage(contact.getPhoneNumber(), null,
                        message, sentIntent, null);

               /* SmsManager smsManager = SmsManager.getDefault();

                PendingIntent piSent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);

                smsManager.sendTextMessage(
                        contact.getPhoneNumber(),
                        null,
                        message,
                        piSent,
                        null);*/

                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();

                input.getText().clear();
                refreshSmsInbox();
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

        } else if(requestCode == SEND_SMS_PERMISSIONS_REQUEST) {
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
        String[] address = {contact.getPhoneNumber()/*, PhoneNumberPrefix.addPrefix(contact.getPhoneNumber())*/};

        ContentResolver contentResolver = getContentResolver();
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms"), null, "address = ?", address, "date");
        int indexBody = smsCursor.getColumnIndex("body");
        int indexIsMe = smsCursor.getColumnIndex("type");
        if (indexBody < 0 || !smsCursor.moveToFirst()) return;
        messageAdapter.clear();

        //TODO remove when finished
        String[] list = smsCursor.getColumnNames();

        do {
            boolean isMe = smsCursor.getInt(indexIsMe) == METYPE;
            Message message = MessageBuilder.aMessage()
                    .withSenderName(contact.getFirstName())
                    .withText(smsCursor.getString(indexBody))
                    .withMe(isMe).build();
            messageAdapter.add(message);
        } while (smsCursor.moveToNext());
    }
}
