package com.vroussea.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
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
import com.vroussea.myapplication.contact.ContactDao;
import com.vroussea.myapplication.contact.ContactDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ContactsActivity extends AppCompatActivity {
    private ContactDao mDao;

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

        displayContacts();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        displayContacts();
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
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Contact> getContacts() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                mDao = ContactDatabase.getDatabase(getApplicationContext()).contactDao();
                return mDao.loadAllContacts();
            }
        };
        Future<List<Contact>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    private void displayContacts() {
        final ListView listview = findViewById(R.id.listview);

        List<Contact> contacts;
        try {
            contacts = getContacts();
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
    }
}
