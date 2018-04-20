package com.vroussea.myapplication.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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

    private int numberOfShownSms = 75;
    private static final int ADDMESSAGES = 1;
    private boolean topOfListAsBeenSeen = false;
    private final int MYTYPE = 2;

    private Cursor smsCursor;

    @SuppressLint("ClickableViewAccessibility")
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

        messages.setOnTouchListener((v, event) -> {
            // get the top of  first child
            View mView = messages.getChildAt(0);
            int top = mView.getTop();

            switch(event.getAction()){

                case MotionEvent.ACTION_MOVE:
                    // see if it top is at Zero, and first visible position is at 0
                    if(top == 0 && messages.getFirstVisiblePosition() == 0 && !topOfListAsBeenSeen){
                        addNewMessages();
                        topOfListAsBeenSeen = true;
                    }
                    else {
                        topOfListAsBeenSeen = false;
                    }
            }
            return false;
        });

        SMSReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                if (bundle != null)
                {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i=0; i<msgs.length; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        Long timeLong = System.currentTimeMillis() / 1000;
                        String timeStamp = timeLong.toString();

                        messageAdapter.add(MessageBuilder.aMessage()
                                .withMe(false)
                                .withSenderName(contact.getFirstName())
                                .withText(msgs[i].getMessageBody())
                                .withTime(getDate(timeStamp)).build());
                        messageAdapter.notifyDataSetChanged();
                        messages.setSelection(messageAdapter.getCount() - 1);
                    }
                }
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

        numberOfShownSms = 50;
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
                //refreshSmsInbox();
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
                //refreshSmsInbox();
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

        String[] address = {contact.getPhoneNumber(), PhoneNumberPrefix.addPrefix(contact.getPhoneNumber())};

        ContentResolver contentResolver = getContentResolver();

        smsCursor = contentResolver.query(Uri.parse("content://sms"), null, "address = ? or address = ?", address, "date");
        int indexBody = smsCursor.getColumnIndex("body");
        int indexIsMe = smsCursor.getColumnIndex("type");
        int indexDate = smsCursor.getColumnIndex("date");
        int cursorSize = smsCursor.getCount();
        if (indexBody < 0 || !smsCursor.moveToPosition(cursorSize - (cursorSize >= numberOfShownSms ? numberOfShownSms : cursorSize))) return;

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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        return formatter.format(new Date(Long.parseLong(timeStamp)));
    }

    private void addNewMessages() {
        int indexBody = smsCursor.getColumnIndex("body");
        int indexIsMe = smsCursor.getColumnIndex("type");
        int indexDate = smsCursor.getColumnIndex("date");
        int cursorSize = smsCursor.getCount();
        if (indexBody < 0 || !smsCursor.moveToPosition(cursorSize - (cursorSize > numberOfShownSms + ADDMESSAGES ? numberOfShownSms + ADDMESSAGES : cursorSize))) return;

        do {
            boolean isMe = smsCursor.getInt(indexIsMe) == MYTYPE;
            Message message = MessageBuilder.aMessage()
                    .withSenderName(contact.getFirstName())
                    .withText(smsCursor.getString(indexBody))
                    .withMe(isMe)
                    .withTime(getDate(smsCursor.getString(indexDate))).build();
             addNewItem(message);

        } while (smsCursor.moveToNext() && smsCursor.getPosition() < cursorSize - numberOfShownSms);
        numberOfShownSms += ADDMESSAGES;
        messageAdapter.notifyDataSetChanged();
    }

    public void addNewItem(Message message) {
        final int positionToSave = messages.getFirstVisiblePosition();
        messageAdapter.insert(message, 0);
        messages.post(() -> messages.setSelection(positionToSave));

        messages.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if(messages.getFirstVisiblePosition() == positionToSave) {
                    messages.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
}
