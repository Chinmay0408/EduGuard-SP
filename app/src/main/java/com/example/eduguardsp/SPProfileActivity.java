package com.example.eduguardsp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class SPProfileActivity extends AppCompatActivity {

    TextView tvAdminName, tvAdminEmail;
    TextView tvTotalColleges, tvTotalTeachers, tvTotalStudents;

    DatabaseReference collegesRef, teachersRef, studentsRef, superAdminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminEmail = findViewById(R.id.tvAdminEmail);
        tvTotalColleges = findViewById(R.id.tvTotalColleges);
        tvTotalTeachers = findViewById(R.id.tvTotalTeachers);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);

        collegesRef = FirebaseDatabase.getInstance().getReference("Admins");
        teachersRef = FirebaseDatabase.getInstance().getReference("Teachers");
        studentsRef = FirebaseDatabase.getInstance().getReference("Students");
        superAdminRef = FirebaseDatabase.getInstance().getReference("SuperAdmins");

        loadSuperAdminProfile();
        loadStatistics();
    }

    private void loadSuperAdminProfile() {
        // Get current logged-in user's UID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        superAdminRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    tvAdminName.setText(name != null ? name : "Unknown");
                    tvAdminEmail.setText(email != null ? email : "No email");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadStatistics() {

        collegesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalColleges.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalTeachers.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalStudents.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
