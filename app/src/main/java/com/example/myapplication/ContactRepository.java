package com.example.myapplication;

import java.util.ArrayList;

/** Dữ liệu chỉ tồn tại trong bộ nhớ của tiến trình, đúng phạm vi đề bài. */
public final class ContactRepository {
    private static final ContactRepository INSTANCE = new ContactRepository();
    private final ArrayList<Contact> contacts = new ArrayList<>();
    ContactRepository() {
        contacts.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, ""));
        contacts.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, ""));
        contacts.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam", false, ""));
    }
    public static ContactRepository getInstance() { return INSTANCE; }
    public ArrayList<Contact> getContacts() { return new ArrayList<>(contacts); }
    public Contact findById(String id) {
        for (Contact contact : contacts) if (contact.getId().equals(id)) return contact;
        return null;
    }
    public boolean containsPhone(String phone) {
        for (Contact contact : contacts) if (contact.getPhone().equals(phone.trim())) return true;
        return false;
    }
    public boolean add(Contact contact) {
        if (!ContactValidator.isValidName(contact.getFullName())
                || !ContactValidator.isValidPhone(contact.getPhone())
                || containsPhone(contact.getPhone())) return false;
        contacts.add(contact);
        return true;
    }
    public void delete(String id) { contacts.removeIf(contact -> contact.getId().equals(id)); }
    public int getFavoriteCount() {
        int count = 0;
        for (Contact contact : contacts) if (contact.isFavorite()) count++;
        return count;
    }
}
