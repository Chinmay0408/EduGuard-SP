package com.example.eduguardsp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;

    FirebaseAuth mAuth;
    DatabaseReference usersRef;

    ProgressDialog dialog;

    // 🔥 HARDCODED SUPER ADMIN
    private final String ADMIN_EMAIL = "superadmin12@gmail.com";
    private final String ADMIN_PASS = "12345678";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);

        checkAutoLogin();

        loginBtn.setOnClickListener(v -> loginUser());
    }

    // ✅ AUTO LOGIN CHECK
    private void checkAutoLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            checkSuperAdmin(user.getUid());
        }
    }

    // ✅ LOGIN FUNCTION
    private void loginUser() {

        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();

        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 HARDCODED LOGIN
        if (userEmail.equals(ADMIN_EMAIL) && userPass.equals(ADMIN_PASS)) {
            Toast.makeText(this, "Hardcoded Admin Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SPMainActivity.class));
            finish();
            return;
        }

        dialog.show();

        // 🔥 FIREBASE AUTH LOGIN
        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        checkSuperAdmin(user.getUid());
                    }

                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this,
                            "Login Failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    // ✅ ROLE CHECK FROM REALTIME DATABASE
    private void checkSuperAdmin(String uid) {

        usersRef.child(uid).child("role")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        dialog.dismiss();

                        if (!snapshot.exists()) {
                            Toast.makeText(LoginActivity.this,
                                    "Role not assigned!",
                                    Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            return;
                        }

                        String role = snapshot.getValue(String.class);

                        if (role == null ||
                                !role.equalsIgnoreCase("Super Admin")) {

                            Toast.makeText(LoginActivity.this,
                                    "Access Denied! Not Super Admin.",
                                    Toast.LENGTH_LONG).show();

                            mAuth.signOut();
                            return;
                        }

                        // ✅ SUCCESS
                        startActivity(new Intent(LoginActivity.this,
                                SPMainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                "Database Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
