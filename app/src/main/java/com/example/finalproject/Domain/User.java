package com.example.finalproject.Domain;

public class User {
    private String Id;
    private String Username;
    private String Email;
    private String Password;

    public User() {
    }

    public User(String id, String username, String email, String password) {
        Id = id;
        Username = username;
        Email = email;
        Password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
