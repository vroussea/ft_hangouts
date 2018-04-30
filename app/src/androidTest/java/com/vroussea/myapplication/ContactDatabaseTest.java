package com.vroussea.myapplication;

//import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.AndroidTestCase;

import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.database.ContactDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

//@RunWith(RobolectricGradleTestRunner.class)
public class ContactDatabaseTest {
    /*private ContactDao mUserDao;
    private ContactDatabase mDb;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, ContactDatabase.class).build();
        mUserDao = mDb.contactDao();
    }

    @After
    public void tearDown() throws Exception {
        mDb.close();
    }

    @Test
    public void ContactHasSameFirstName() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("george");
        mUserDao.insert(contact);
        List<Contact> byName = mUserDao.findUsersByName("george");
        assertThat(byName.get(0).getFirstName(), equalTo(contact.getFirstName()));
    }

    @Test
    public void ContactHasSameLastName() throws Exception {
        Contact contact = new Contact();
        contact.setLastName("george");
        mUserDao.insert(contact);
        List<Contact> byName = mUserDao.findUsersByName("george");
        assertThat(byName.get(0).getFirstName(), equalTo(contact.getFirstName()));
    }

    @Test    public void findTwoContacts() throws Exception {
        Contact contact = new Contact();
        contact.setLastName("george");
        mUserDao.insert(contact);
        Contact contact1 = new Contact();
        contact1.setFirstName("george");
        mUserDao.insert(contact1);
        List<Contact> byName = mUserDao.findUsersByName("george");
        assertThat(byName.size(), equalTo(2));
    }

    @Test
    public void findNoContact() throws Exception {
        List<Contact> byName = mUserDao.findUsersByName("george");
        assertThat(byName.size(), equalTo(0));
    }

    @Test
    public void AddMultipleContacts() throws Exception {
        Contact contact = new Contact();
        contact.setLastName("george");
        mUserDao.insert(contact);
        Contact contact1 = new Contact();
        contact1.setLastName("george1");
        mUserDao.insert(contact1);
        Contact contact2 = new Contact();
        contact2.setLastName("george2");
        mUserDao.insert(contact2);
        List<Contact> byName = mUserDao.loadAllContacts();
        assertThat(byName.size(), equalTo(3));
    }*/
}