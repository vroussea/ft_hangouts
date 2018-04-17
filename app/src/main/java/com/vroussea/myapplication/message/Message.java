package com.vroussea.myapplication.message;

public class Message {
    private String senderName;
    private String time;
    private String text;
    private boolean me;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }
}

