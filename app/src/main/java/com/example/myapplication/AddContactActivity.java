package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddContactActivity extends BaseActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_add_contact);
        Spinner group = findViewById(R.id.groupInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contact_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapter);
        clearErrorWhenEdited(findViewById(R.id.nameInput));
        clearErrorWhenEdited(findViewById(R.id.phoneInput));
        findViewById(R.id.saveContactButton).setOnClickListener(view -> save());
    }
    private void clearErrorWhenEdited(EditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable text) { input.setError(null); }
        });
    }
    private void save() {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText phoneInput = findViewById(R.id.phoneInput);
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        nameInput.setError(null);
        phoneInput.setError(null);
        if (!ContactValidator.isValidName(name)) {
            nameInput.setError(getString(R.string.name_required)); nameInput.requestFocus(); return;
        }
        if (!ContactValidator.isValidPhone(phone)) {
            phoneInput.setError(getString(R.string.phone_invalid)); phoneInput.requestFocus(); return;
        }
        ContactRepository repository = ContactRepository.getInstance();
        if (repository.containsPhone(phone)) {
            phoneInput.setError(getString(R.string.phone_duplicate)); phoneInput.requestFocus(); return;
        }
        Spinner group = findViewById(R.id.groupInput);
        RadioGroup gender = findViewById(R.id.genderInput);
        RadioButton selected = findViewById(gender.getCheckedRadioButtonId());
        CheckBox favorite = findViewById(R.id.favoriteInput);
        EditText notes = findViewById(R.id.notesInput);
        Contact contact = new Contact(name, phone, group.getSelectedItem().toString(),
                selected.getText().toString(), favorite.isChecked(), notes.getText().toString());
        if (repository.add(contact)) {
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
