package com.vroussea.myapplication.contact.database;

import android.provider.BaseColumns;

public final class ContactContract {
    private ContactContract() {
    }

    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_FIRSTNAME = "first_name";
        public static final String COLUMN_LASTNAME = "last_name";
        public static final String COLUMN_PHONENUMBER = "phone_number";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_NICKNAME = "nickname";
        public static final String COLUMN_PICTURE = "picture";
    }
}
