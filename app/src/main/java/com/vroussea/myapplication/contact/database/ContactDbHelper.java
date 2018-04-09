package com.vroussea.myapplication.contact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " (" +
                    ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactContract.ContactEntry.COLUMN_FIRSTNAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_LASTNAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_PHONENUMBER + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_EMAIL + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_NICKNAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactContract.ContactEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact.db";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
