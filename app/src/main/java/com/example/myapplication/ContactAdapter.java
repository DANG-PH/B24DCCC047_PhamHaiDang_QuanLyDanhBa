package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private ArrayList<Contact> contacts = new ArrayList<>();
    public ContactAdapter(Context context) { inflater = LayoutInflater.from(context); }
    public void submit(ArrayList<Contact> contacts) { this.contacts = contacts; notifyDataSetChanged(); }
    @Override public int getCount() { return contacts.size(); }
    @Override public Contact getItem(int position) { return contacts.get(position); }
    @Override public long getItemId(int position) { return position; }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? inflater.inflate(R.layout.item_contact, parent, false) : convertView;
        Contact contact = getItem(position);
        ((TextView) view.findViewById(R.id.avatar)).setText(contact.getInitial());
        ((TextView) view.findViewById(R.id.contactName)).setText(contact.getFullName());
        ((TextView) view.findViewById(R.id.contactPhone)).setText(contact.getPhone());
        ((TextView) view.findViewById(R.id.contactGroup)).setText(contact.getGroup());
        View star = view.findViewById(R.id.favoriteStar);
        star.setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);
        return view;
    }
}
