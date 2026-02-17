package com.example.eduguardsp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddAdminActivity extends AppCompatActivity {

    EditText etName, etCollegeName, etEmail, etPhone;
    Button btnCreate;

    FirebaseAuth mAuth;
    DatabaseReference usersRef, adminsRef;

    private final String DEFAULT_PASS = "admin@123"; // ⭐ default password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        etName = findViewById(R.id.etName);
        etCollegeName = findViewById(R.id.etClgName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnCreate = findViewById(R.id.btnCreateTeacher);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        adminsRef = FirebaseDatabase.getInstance().getReference("Admins");

        btnCreate.setOnClickListener(v -> createTeacher());
    }

    private void createTeacher(){

        String name = etName.getText().toString().trim();
        String clgname = etCollegeName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if(name.isEmpty() || clgname.isEmpty() || email.isEmpty() || phone.isEmpty()){
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 Create Firebase Auth User
        mAuth.createUserWithEmailAndPassword(email, DEFAULT_PASS)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user == null){
                            Toast.makeText(this,"Error creating user",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String uid = user.getUid();

                        saveAdminToDatabase(uid, name, clgname, email, phone);

                    }else{
                        Toast.makeText(this,
                                "Error: "+task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveAdminToDatabase(String uid,
                                       String name,
                                       String clgname,
                                       String email,
                                       String phone){

        // Teacher Data
        HashMap<String,Object> adminMap = new HashMap<>();
        adminMap.put("name",name);
        adminMap.put("collegeName",clgname);
        adminMap.put("email",email);
        adminMap.put("phone",phone);
        adminMap.put("createdAt",System.currentTimeMillis());

        adminsRef.child(uid).setValue(adminMap);

        // Role Assignment
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("role","admin");
        userMap.put("email",email);

        usersRef.child(uid).setValue(userMap)
                .addOnSuccessListener(unused -> {

                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(this,
                            "Admin Created!\nDefault Password: "+DEFAULT_PASS,
                            Toast.LENGTH_LONG).show();

                    finish();
                });
    }
}
