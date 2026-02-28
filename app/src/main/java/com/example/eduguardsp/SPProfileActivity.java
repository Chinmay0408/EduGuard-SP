package com.example.eduguardsp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class SPProfileActivity extends AppCompatActivity {

    TextView tvAdminName, tvAdminEmail;
    TextView tvTotalColleges, tvTotalTeachers, tvTotalStudents;

    DatabaseReference collegesRef, teachersRef, studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile); // 🔥 Make sure this matches your XML file name

        // 🔥 Initialize Views
        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminEmail = findViewById(R.id.tvAdminEmail);
        tvTotalColleges = findViewById(R.id.tvTotalColleges);
        tvTotalTeachers = findViewById(R.id.tvTotalTeachers);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);


        // 🔥 Firebase References (ONLY for statistics)
        collegesRef = FirebaseDatabase.getInstance()
                .getReference("Admins");

        teachersRef = FirebaseDatabase.getInstance()
                .getReference("Teachers");

        studentsRef = FirebaseDatabase.getInstance()
                .getReference("Students");

        loadStatistics();
    }

    private void loadStatistics(){

        // 🔥 Count Colleges
        collegesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvTotalColleges.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 🔥 Count Teachers
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvTotalTeachers.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 🔥 Count Students
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvTotalStudents.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}