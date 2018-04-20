package com.vroussea.myapplication.contact.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.database.ContactContract.ContactEntry;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;

    public ContactDao(SQLiteOpenHelper databaseHelper) {
        writeDatabase = databaseHelper.getWritableDatabase();
        readDatabase = databaseHelper.getReadableDatabase();
    }

    public void insert(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_FIRSTNAME, contact.getFirstName());
        values.put(ContactEntry.COLUMN_LASTNAME, contact.getLastName());
        values.put(ContactEntry.COLUMN_EMAIL, contact.getEMail());
        values.put(ContactEntry.COLUMN_NICKNAME, contact.getNickname());
        values.put(ContactEntry.COLUMN_PHONENUMBER, contact.getPhoneNumber());
        values.put(ContactEntry.COLUMN_PICTURE, contact.getPicture());

        writeDatabase.insert(ContactEntry.TABLE_NAME, null, values);
    }

    public List<Contact> querryAll() {
        Cursor cursor = readDatabase.rawQuery("select * from " + ContactEntry.TABLE_NAME, null);

        List<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry._ID)));
            contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_FIRSTNAME)));
            contact.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONENUMBER)));
            contact.setPicture(cursor.getBlob(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PICTURE)));
            contacts.add(contact);
        }
        cursor.close();

        return contacts;
    }

    public Contact querryOneById(int id) {
        Cursor cursor = readDatabase.rawQuery("SELECT * FROM " + ContactEntry.TABLE_NAME + " WHERE " + ContactEntry._ID + " = " + id, null);

        Contact contact = new Contact();
        cursor.moveToFirst();

        contact.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry._ID)));
        contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_FIRSTNAME)));
        contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_LASTNAME)));
        contact.setNickname(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_NICKNAME)));
        contact.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONENUMBER)));
        contact.setEMail(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_EMAIL)));
        contact.setPicture(cursor.getBlob(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PICTURE)));

        cursor.close();

        return contact;
    }

    public Contact querryOneByPhoneNumber(String phoneNumber) {
        Cursor cursor = readDatabase.rawQuery("SELECT * FROM " + ContactEntry.TABLE_NAME + " WHERE " + ContactEntry.COLUMN_PHONENUMBER + " = " + phoneNumber, null);

        Contact contact = new Contact();
        cursor.moveToFirst();

        contact.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(ContactEntry._ID)));
        contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_FIRSTNAME)));
        contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_LASTNAME)));
        contact.setNickname(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_NICKNAME)));
        contact.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONENUMBER)));
        contact.setEMail(cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_EMAIL)));
        contact.setPicture(cursor.getBlob(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PICTURE)));

        cursor.close();

        return contact;
    }

    public void delete(int id) {
        readDatabase.delete(ContactEntry.TABLE_NAME, ContactEntry._ID + " = " + id, null);
    }

    public void update(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_FIRSTNAME, contact.getFirstName());
        values.put(ContactEntry.COLUMN_LASTNAME, contact.getLastName());
        values.put(ContactEntry.COLUMN_EMAIL, contact.getEMail());
        values.put(ContactEntry.COLUMN_NICKNAME, contact.getNickname());
        values.put(ContactEntry.COLUMN_PHONENUMBER, contact.getPhoneNumber());
        values.put(ContactEntry.COLUMN_PICTURE, contact.getPicture());

        writeDatabase.update(ContactEntry.TABLE_NAME, values, ContactEntry._ID + " = ?", new String[]{String.valueOf(contact.get_id())});
    }
}
