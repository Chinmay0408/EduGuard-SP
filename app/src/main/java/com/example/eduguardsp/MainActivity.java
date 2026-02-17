package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        new android.os.Handler().postDelayed(this::checkUser, 1500);
    }

    private void checkUser() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        usersRef.child(user.getUid()).child("role")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            mAuth.signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            return;
                        }

                        String role = snapshot.getValue(String.class);

                        if (role != null && role.equalsIgnoreCase("super admin")) {

                            startActivity(new Intent(MainActivity.this,
                                    SPMainActivity.class));
                            finish();

                        } else {
                            mAuth.signOut();
                            startActivity(new Intent(MainActivity.this,
                                    LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this,
                                LoginActivity.class));
                        finish();
                    }
                });
    }
}
