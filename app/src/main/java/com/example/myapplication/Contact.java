package com.example.myapplication;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

/** Mô hình một liên hệ, có thể truyền qua Intent. */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String fullName;
    private final String phone;
    private final String group;
    private final String gender;
    private boolean favorite;
    private final String notes;

    public Contact(String fullName, String phone, String group, String gender,
                   boolean favorite, String notes) {
        this.id = UUID.randomUUID().toString();
        this.fullName = fullName.trim();
        this.phone = phone.trim();
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.notes = notes.trim();
    }
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGroup() { return group; }
    public String getGender() { return gender; }
    public boolean isFavorite() { return favorite; }
    public String getNotes() { return notes; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public String getInitial() {
        String[] words = fullName.trim().split("\\s+");
        String name = words[words.length - 1];
        return name.isEmpty() ? "?" : name.substring(0, name.offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT);
    }
}
