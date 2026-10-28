package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
    // Tài khoản cố định theo đề bài, không dùng dịch vụ xác thực.
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_login);
        findViewById(R.id.loginButton).setOnClickListener(view -> login());
        ((EditText) findViewById(R.id.passwordInput)).setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_DONE) { login(); return true; }
            return false;
        });
    }
    private void login() {
        EditText username = findViewById(R.id.usernameInput);
        EditText password = findViewById(R.id.passwordInput);
        TextView error = findViewById(R.id.loginError);
        String name = username.getText().toString().trim();
        String pass = password.getText().toString();
        if (name.isEmpty() || pass.isEmpty()) {
            error.setText(R.string.login_empty);
            error.setVisibility(View.VISIBLE);
        } else if (!USERNAME.equals(name) || !PASSWORD.equals(pass)) {
            error.setText(R.string.login_wrong);
            error.setVisibility(View.VISIBLE);
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
