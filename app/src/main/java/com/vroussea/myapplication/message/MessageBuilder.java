package com.vroussea.myapplication.message;

public final class MessageBuilder {
    private String senderName;
    private String time;
    private String text;
    private boolean me;

    private MessageBuilder() {
    }

    public static MessageBuilder aMessage() {
        return new MessageBuilder();
    }

    public MessageBuilder withSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public MessageBuilder withTime(String time) {
        this.time = time;
        return this;
    }

    public MessageBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder withMe(boolean me) {
        this.me = me;
        return this;
    }

    public Message build() {
        Message message = new Message();
        message.setSenderName(senderName);
        message.setTime(time);
        message.setText(text);
        message.setMe(me);
        return message;
    }
}
