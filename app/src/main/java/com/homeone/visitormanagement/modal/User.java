package com.homeone.visitormanagement.modal;

public class User {
    public String name;
    public String role;
    public String email;
    public String token;
    public String password;

    public User(){

    }
    public User(String name, String role, String email, String password) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }
    public User(String name, String role, String email, String password,String token) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
        this.token = token;
    }
}
