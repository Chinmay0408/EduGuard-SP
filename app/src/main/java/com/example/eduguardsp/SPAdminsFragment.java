package com.example.eduguardsp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SPAdminsFragment extends Fragment {

    RecyclerView recyclerView;
    AdminAdapter adapter;
    ArrayList<Admin> adminList;
    FloatingActionButton fab;
    DatabaseReference adminRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sp_admins, container, false);

        recyclerView = view.findViewById(R.id.adminRecycler);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        adminList = new ArrayList<>();
        adapter = new AdminAdapter(adminList, admin -> {

            Intent intent = new Intent(
                    getContext(),
                    AdminDetailActivity.class);

            intent.putExtra("name", admin.name);
            intent.putExtra("collegeName", admin.collegeName);

            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        adminRef = FirebaseDatabase.getInstance()
                .getReference("Admins");

        loadAdmin();

        fab = view.findViewById(R.id.fabAddAdmin);

        fab.setOnClickListener(v -> {

            startActivity(new Intent(
                    getContext(),
                    AddAdminActivity.class));

        });

        return view;
    }

    private void loadAdmin(){

        adminRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                adminList.clear();

                for(DataSnapshot ds : snapshot.getChildren()){

                    Admin admin = ds.getValue(Admin.class);
                    admin.uid = ds.getKey();
                    adminList.add(admin);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
