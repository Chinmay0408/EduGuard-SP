package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    Button registerBtn;
    TextView signinPage;

    FirebaseAuth auth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        signinPage = findViewById(R.id.signin_page);

        auth = FirebaseAuth.getInstance();

        // ✅ SAVE INTO SuperAdmins (NO SPACE)
        usersRef = FirebaseDatabase.getInstance()
                .getReference("SuperAdmins");

        signinPage.setOnClickListener(v -> {
            startActivity(new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            ));
        });

        registerBtn.setOnClickListener(v -> registerUser());
    }


    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String uid = auth.getCurrentUser().getUid();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uid", uid);                 // ✅ important
                        map.put("name", name);
                        map.put("email", email);
                        map.put("phone", phone);
                        map.put("password", password);       // ✅ needed for web login
                        map.put("role", "Super Admin");
                        map.put("createdAt", System.currentTimeMillis());

                        usersRef.child(uid).setValue(map)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(
                                            this,
                                            "Registered Successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    startActivity(new Intent(
                                            this,
                                            LoginActivity.class
                                    ));

                                    finish();
                                });

                    } else {

                        Toast.makeText(
                                this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}