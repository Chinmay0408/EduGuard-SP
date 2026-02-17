package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.*;

public class SPHomeFragment extends Fragment {

    TextView tvAdmins, tvStudents, tvFaculty,
            tvColleges, tvClasses, tvRisk;

    DatabaseReference adminsRef, studentsRef,
            teachersRef, collegesRef,
            classesRef, riskRef;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sp_home, container, false);

        // 🔥 Initialize Views
        tvAdmins = view.findViewById(R.id.tvAdmins);
        tvStudents = view.findViewById(R.id.tvTotalStudents);
        tvFaculty = view.findViewById(R.id.tvTotalFaculty);
        tvColleges = view.findViewById(R.id.tvColleges);
        tvClasses = view.findViewById(R.id.tvTotalClasses);
        tvRisk = view.findViewById(R.id.tvRiskAlerts);

        // 🔥 Firebase References
        adminsRef = FirebaseDatabase.getInstance().getReference("Admins");
        studentsRef = FirebaseDatabase.getInstance().getReference("Students");
        teachersRef = FirebaseDatabase.getInstance().getReference("Teachers");
        collegesRef = FirebaseDatabase.getInstance().getReference("Admins");  //currently using admin ref just because 1 admin = 1 college and doesnt have firebase access
        classesRef = FirebaseDatabase.getInstance().getReference("Classes");
        riskRef = FirebaseDatabase.getInstance().getReference("RiskAlerts");

        loadCounts();

        // 🔥 Quick Actions
        view.findViewById(R.id.btnAddTeacher)
                .setOnClickListener(v ->
                        startActivity(new Intent(getActivity(), AddAdminActivity.class)));

        view.findViewById(R.id.btnNotice)
                .setOnClickListener(v ->
                        startActivity(new Intent(getActivity(), NoticeActivity.class)));

        return view;
    }

    // 🔥 Load All Counts
    private void loadCounts() {

        loadCount(adminsRef, tvAdmins);
        loadCount(studentsRef, tvStudents);
        loadCount(teachersRef, tvFaculty);
        loadCount(collegesRef, tvColleges);
        loadCount(classesRef, tvClasses);
        loadCount(riskRef, tvRisk);
    }

    // 🔥 Generic Count Loader
    private void loadCount(DatabaseReference ref, TextView textView) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                textView.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                textView.setText("0");
            }
        });
    }
}
