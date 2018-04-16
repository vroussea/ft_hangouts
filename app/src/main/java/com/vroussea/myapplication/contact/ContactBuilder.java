package com.vroussea.myapplication.contact;

final public class ContactBuilder {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String eMail;
    private String nickname;
    private byte[] profilePic;

    private ContactBuilder() {
    }

    public static ContactBuilder aContact() {
        return new ContactBuilder();
    }

    public ContactBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ContactBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ContactBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ContactBuilder withEMail(String eMail) {
        this.eMail = eMail;
        return this;
    }

    public ContactBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ContactBuilder withProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
        return this;
    }

    public Contact build() {
        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhoneNumber(phoneNumber);
        contact.setEMail(eMail);
        contact.setNickname(nickname);
        contact.setPicture(profilePic);
        return contact;
    }
}
