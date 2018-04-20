package com.vroussea.myapplication.contact;

import android.app.Activity;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.contact.database.ContactDao;
import com.vroussea.myapplication.contact.database.ContactDbHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ContactHelper extends Activity {

    private ContactDao mDao = new ContactDao(new ContactDbHelper(App.getContext()));

    public List<Contact> getContacts() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Contact>> callable = () -> mDao.querryAll();
        Future<List<Contact>> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    public Contact getContactById(int id) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Contact> callable = () -> mDao.querryOneById(id);
        Future<Contact> future = executor.submit(callable);
        executor.shutdown();

        return future.get();
    }

    public Contact getContactByPhoneNumber(String phoneNumber) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Contact> callable = () -> mDao.querryOneByPhoneNumber(phoneNumber);
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
            mDao.delete(contact.get_id());
        }).start();
    }
}
