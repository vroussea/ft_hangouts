package com.vroussea.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private final Context context;
    private final List<Contact> contacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, -1, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate a new lite contact layout
        View rowView = inflater.inflate(R.layout.lite_contact, parent, false);

        // retrieve fields in contact view
        TextView firstName = rowView.findViewById(R.id.firstLine);
        TextView phonenumber = rowView.findViewById(R.id.secondLine);
        //ImageView imageView = rowView.findViewById(R.id.icon);


        firstName.setText(contacts.get(position).getFirstName());
        phonenumber.setText(contacts.get(position).getPhoneNumber());
        //imageView = contacts.get(position).getProfilePic();



        return rowView;
    }

}
