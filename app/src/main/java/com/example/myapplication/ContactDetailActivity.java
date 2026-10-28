package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.IntentCompat;

public class ContactDetailActivity extends BaseActivity {
    public static final String EXTRA_CONTACT = "com.example.myapplication.CONTACT";
    private Contact contact;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_contact_detail);
        Contact received = IntentCompat.getSerializableExtra(getIntent(), EXTRA_CONTACT, Contact.class);
        contact = received == null ? null : ContactRepository.getInstance().findById(received.getId());
        if (contact == null) {
            Toast.makeText(this, R.string.missing_contact, Toast.LENGTH_SHORT).show(); finish(); return;
        }
        ((TextView) findViewById(R.id.detailAvatar)).setText(contact.getInitial());
        ((TextView) findViewById(R.id.detailName)).setText(contact.getFullName());
        ((TextView) findViewById(R.id.detailPhone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.detailGroup)).setText(contact.getGroup());
        ((TextView) findViewById(R.id.detailGender)).setText(contact.getGender());
        ((TextView) findViewById(R.id.detailNotes)).setText(contact.getNotes().isEmpty()
                ? getString(R.string.no_notes) : contact.getNotes());
        CheckBox favorite = findViewById(R.id.detailFavorite);
        favorite.setChecked(contact.isFavorite());
        favorite.setOnCheckedChangeListener((button, checked) -> contact.setFavorite(checked));
        findViewById(R.id.callButton).setOnClickListener(view -> {
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.getPhone(), null));
            try { startActivity(dial); }
            catch (ActivityNotFoundException exception) {
                Toast.makeText(this, R.string.dial_unavailable, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.deleteButton).setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, contact.getFullName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete_contact, (dialog, which) -> {
                    ContactRepository.getInstance().delete(contact.getId());
                    Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show(); finish();
                }).show());
    }
}
