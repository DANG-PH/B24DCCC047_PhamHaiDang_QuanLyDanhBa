package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private ContactAdapter adapter;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_main);
        ListView list = findViewById(R.id.contactList);
        adapter = new ContactAdapter(this);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.emptyContacts));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.EXTRA_CONTACT, adapter.getItem(position));
            startActivity(intent);
        });
        findViewById(R.id.addContactButton).setOnClickListener(view ->
                startActivity(new Intent(this, AddContactActivity.class)));
    }
    @Override protected void onResume() {
        super.onResume();
        ContactRepository repository = ContactRepository.getInstance();
        adapter.submit(repository.getContacts());
        ((TextView) findViewById(R.id.totalCount)).setText(getString(R.string.total_format, adapter.getCount()));
        ((TextView) findViewById(R.id.favoriteCount)).setText(getString(R.string.favorites_format, repository.getFavoriteCount()));
    }
}
