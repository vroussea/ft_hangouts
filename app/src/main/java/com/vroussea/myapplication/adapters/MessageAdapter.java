package com.vroussea.myapplication.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.R;
import com.vroussea.myapplication.message.Message;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private final Context context;

    private ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> _messages) {
        super(context, -1, _messages);
        this.context = context;
        this.messages = _messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.message, parent, false);

        TextView firstLine = rowView.findViewById(R.id.firstLine);
        TextView secondLine = rowView.findViewById(R.id.secondLine);

        boolean isMe = messages.get(position).getMe();
        if (isMe) {
            firstLine.setText(App.getContext().getString(R.string.me) + " " + messages.get(position).getTime());
            secondLine.setText(messages.get(position).getText().trim());
            firstLine.setGravity(Gravity.END);
            secondLine.setGravity(Gravity.END);
        } else {
            firstLine.setText(messages.get(position).getSenderName() + " " + messages.get(position).getTime());
            secondLine.setText(messages.get(position).getText().trim());
        }


        return rowView;
    }

    @Override
    public void add(Message message) {
        super.add(message);
        //messages.add(message);
    }

}
