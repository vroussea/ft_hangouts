package com.vroussea.myapplication.contact;

import android.app.Activity;

import com.vroussea.myapplication.App;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ContactHelper extends Activity {

    private ContactDao mDao = ContactDatabase.getDatabase(App.getContext()).contactDao();

    public List<Contact> getContacts() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return mDao.loadAllContacts();
            }
        };
        Future<List<Contact>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    public Contact getContactById(int id) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Contact> callable = new Callable<Contact>() {
            @Override
            public Contact call() {
                return mDao.findContactById(id);
            }
        };
        Future<Contact> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    public void addContact(Contact contact) {
        new Thread(() -> {
            mDao.insert(contact);
        }).start();
    }

    public void updateContact(Contact contact) {
        new Thread(() -> {
            mDao.update(contact);
        }).start();
    }

    public void removeContact(Contact contact) {
        new Thread(() -> {
            mDao.delete(contact);
        }).start();
    }
}
