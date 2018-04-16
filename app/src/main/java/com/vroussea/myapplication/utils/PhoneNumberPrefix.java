package com.vroussea.myapplication.utils;

public class PhoneNumberPrefix {
    public static String addPrefix(String phoneNumber) {
        if (phoneNumber.contains("+"))
            return phoneNumber;
        return phoneNumber.replaceFirst("\\d", "+33");
    }

    public static String removePrefix(String phoneNumber) {
        if (!phoneNumber.contains("+"))
            return phoneNumber;
        return phoneNumber.replaceFirst(".\\d\\d", "0");
    }
}
