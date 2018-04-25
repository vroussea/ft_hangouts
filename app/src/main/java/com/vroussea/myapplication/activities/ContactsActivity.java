package com.vroussea.myapplication.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.R;
import com.vroussea.myapplication.adapters.ContactAdapter;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactBuilder;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.utils.BitmapToBytes;
import com.vroussea.myapplication.utils.Colors;
import com.vroussea.myapplication.utils.PhoneNumberPrefix;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    ContactHelper contactHelper = new ContactHelper();
    private BroadcastReceiver SMSReceiver;

    private final int RECEIVE_SMS_PERMISSIONS_REQUEST = 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar myToolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        myToolbar.setTitle("");

        displayContacts(new Intent(this, ContactDisplay.class));

        getPermissionToReceiveSMS();

        contactHelper = new ContactHelper();
        SMSReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String phoneNumber = PhoneNumberPrefix.removePrefix(msgs[i].getDisplayOriginatingAddress());
                        try {
                            //if (contactHelper.getContactByPhoneNumber(phoneNumber) == null) {
                            Contact contact = ContactBuilder.aContact()
                                    .withEMail("")
                                    .withLastName("")
                                    .withNickname("")
                                    .withPhoneNumber(phoneNumber)
                                    .withFirstName(phoneNumber)
                                    .withProfilePic(BitmapToBytes.getBytes(BitmapFactory.decodeResource(getResources(), R.drawable.no_picture))).build();
                            for (Contact contactIterator : contactHelper.getContacts()) {
                                if (contactIterator.getPhoneNumber().equals(phoneNumber)) {
                                    contact = null;
                                    break;
                                }
                            }

                            if (contact != null) { contactHelper.addContact(contact);
                                displayContacts(new Intent(App.getContext(), ContactDisplay.class));
                            }
                            //}
                        } catch (Exception e) {
                            Log.d("error with sms contact", e.getMessage());
                        }
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(10000);

        registerReceiver(SMSReceiver, filter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        displayContacts(new Intent(this, ContactDisplay.class));
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
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(SMSReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_contact) {
            Intent intent = new Intent(this, ContactEdit.class);
            intent.putExtra("isCreating", true);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayContacts(Intent intent) {
        final ListView listview = findViewById(R.id.listview);

        List<Contact> contacts;
        try {
            contacts = contactHelper.getContacts();
        } catch (Exception e) {
            finish();
            return;
        }

        final ContactAdapter adapter = new ContactAdapter(this, contacts);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener((parent, view, position, id) -> {
            final Contact item = (Contact) parent.getItemAtPosition(position);
            view.animate().alpha(0).withEndAction(() -> {
                intent.putExtra("contact", item.get_id());
                adapter.notifyDataSetChanged();
                view.setAlpha(1);
                startActivity(intent);
            });
        });
    }

    public void getPermissionToReceiveSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                    RECEIVE_SMS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == RECEIVE_SMS_PERMISSIONS_REQUEST) {
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
}
