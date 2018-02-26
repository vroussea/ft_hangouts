package com.vroussea.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.adapters.ContactAdapter;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactBuilder;
import com.vroussea.myapplication.contact.ContactDao;
import com.vroussea.myapplication.contact.ContactDatabase;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    ContactDao mDao;

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

        Toolbar myToolbar = findViewById(R.id.menu_contact);
        setSupportActionBar(myToolbar);

        final ListView listview = findViewById(R.id.listview);

        new Thread(() -> {
            mDao = ContactDatabase.getDatabase(getApplicationContext()).contactDao();

            Contact contact1 = ContactBuilder.aContact().withFirstName("Jean").withPhoneNumber("0103040304").build();
            Contact contact2 = ContactBuilder.aContact().withFirstName("Jack").withPhoneNumber("0103040304").build();

            mDao.insert(contact1, contact2);

            final List<Contact> contacts = mDao.loadAllContacts();

            final ContactAdapter adapter = new ContactAdapter(this, contacts);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final Contact item = (Contact) parent.getItemAtPosition(position);
                    view.animate().alpha(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            contacts.remove(item);
                            new Thread(() -> {
                                mDao.delete(item);
                            }).start();
                            adapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        }
                    });
                }
            });
        }).start();
    }
}
