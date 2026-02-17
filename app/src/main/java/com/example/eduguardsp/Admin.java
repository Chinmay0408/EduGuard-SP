package com.example.eduguardsp;

public class Admin {
    public String uid;
    public String name;
    public String collegeName;

    public Admin() {
        // Needed for Firebase
    }

    public Admin(String uid,String name, String collegeName) {
        this.uid = uid;
        this.name = name;
        this.collegeName = collegeName;
    }
}

