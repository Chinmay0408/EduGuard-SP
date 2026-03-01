package com.example.eduguardsp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class AdminDetailActivity extends AppCompatActivity {

    TextView tvName, tvCollege, tvStudentCount, tvFacultyCount;

    DatabaseReference teachersRef, studentsRef;

    String collegeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);

        tvName = findViewById(R.id.tvTeacherName);
        tvCollege = findViewById(R.id.tvClass);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        tvFacultyCount = findViewById(R.id.tvFacultyCount);

        String name = getIntent().getStringExtra("name");
        collegeName = getIntent().getStringExtra("collegeName");

        if (name == null) name = "Unknown";
        if (collegeName == null) collegeName = "";

        tvName.setText(name);
        tvCollege.setText("College: " + collegeName);

        teachersRef = FirebaseDatabase.getInstance().getReference("Teachers");
        studentsRef = FirebaseDatabase.getInstance().getReference("Students");

        loadFacultyCount();
        loadStudentCount();
    }

    private void loadFacultyCount() {
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvFacultyCount.setText("Faculty: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadStudentCount() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvStudentCount.setText("Students: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
