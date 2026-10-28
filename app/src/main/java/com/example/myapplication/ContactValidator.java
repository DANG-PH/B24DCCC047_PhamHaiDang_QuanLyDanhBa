package com.example.myapplication;

public final class ContactValidator {
    private ContactValidator() { }
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.trim().matches("0[0-9]{9}");
    }
}
