package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 🔥 make sure layout name matches

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null &&
                                user.getEmail().equals("superadmin12@gmail.com")) {

                            // ✅ Super Admin Login Successful
                            Toast.makeText(this,
                                    "Welcome Super Admin",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    SPMainActivity.class // 🔥 replace with your dashboard activity
                            );

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            // ❌ Not Super Admin
                            Toast.makeText(this,
                                    "Access Denied",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseAuth.getInstance().signOut();
                        }

                    } else {
                        // ❌ Wrong Credentials
                        Toast.makeText(this,
                                "Invalid Email or Password",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}