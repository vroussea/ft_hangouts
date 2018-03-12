package com.vroussea.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.adapters.ContactAdapter;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactHelper;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    ContactHelper contactHelper = new ContactHelper();

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        myToolbar.setTitle("");

        displayContacts(new Intent(this, ContactDisplay.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        displayContacts(new Intent(this, ContactDisplay.class));
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
            //Intent intent = new Intent(this, SettingsActivity.class);
            //startActivity(intent);
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Contact item = (Contact) parent.getItemAtPosition(position);
                view.animate().alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        intent.putExtra("contact", item.get_id());
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
