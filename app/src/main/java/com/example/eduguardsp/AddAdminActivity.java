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
    DatabaseReference usersRef, superAdminRef; // 🔥 Updated to superAdminRef

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

        // References to your Realtime Database nodes
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        // 🔥 Changed this to target the "Super Admin" collection/node
        superAdminRef = FirebaseDatabase.getInstance().getReference("Super Admin");

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

                    } else {
                        Toast.makeText(this,
                                "Error: "+task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String generateAdminId(String uid){
        return "ADM_" + uid.substring(0,5).toUpperCase();
    }

    private void saveAdminToDatabase(String uid,
                                     String name,
                                     String clgname,
                                     String email,
                                     String phone){

        String adminId = generateAdminId(uid);

        // Admin Data Map
        HashMap<String,Object> adminMap = new HashMap<>();
        adminMap.put("adminId", adminId);
        adminMap.put("name",name);
        adminMap.put("collegeName",clgname);
        adminMap.put("email",email);
        adminMap.put("phone",phone);
        adminMap.put("createdAt",System.currentTimeMillis());

        // 🔥 Saving the data to the "Super Admin" node using the generated UID
        superAdminRef.child(uid).setValue(adminMap);

        // Role Assignment (Saved in "Users" so your login system knows this UID belongs to an admin)
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("role","admin");
        userMap.put("email",email);
        userMap.put("adminId", adminId);

        usersRef.child(uid).setValue(userMap)
                .addOnSuccessListener(unused -> {

                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(this,
                            "Admin Created!\nID: " + adminId +
                                    "\nPassword: "+DEFAULT_PASS,
                            Toast.LENGTH_LONG).show();

                    finish();
                });
    }
}