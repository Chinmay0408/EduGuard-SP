package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView signupPage;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 🔥 make sure layout name matches

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginBtn);
        signupPage = findViewById(R.id.signup_page);

        mAuth = FirebaseAuth.getInstance();

        signupPage.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();

                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("SuperAdmins")
                                .child(uid)
                                .child("role");

                        ref.get().addOnSuccessListener(snapshot -> {

                            if (snapshot.exists()) {

                                String role = snapshot.getValue(String.class);

                                if ("Super Admin".equals(role)) {

                                    Toast.makeText(this,
                                            "Welcome Super Admin",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(
                                            LoginActivity.this,
                                            SPMainActivity.class
                                    );

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intent);

                                } else {

                                    Toast.makeText(this,
                                            "Access Denied",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseAuth.getInstance().signOut();
                                }

                            } else {

                                Toast.makeText(this,
                                        "No Role Found",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseAuth.getInstance().signOut();
                            }
                        });

                    } else {

                        Toast.makeText(this,
                                "Invalid Email or Password",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}