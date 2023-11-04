package com.example.myapplication;

public class Contact {
    private String name;
    private String dob;
    private String email;
    private String profileImageResource;

    public Contact(String name, String dob, String email, String profileImageResource) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.profileImageResource = profileImageResource;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageResource() {
        return profileImageResource;
    }
}

