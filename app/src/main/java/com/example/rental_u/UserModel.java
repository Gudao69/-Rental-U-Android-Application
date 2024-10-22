package com.example.rental_u;

public class UserModel {

    int user_id;

    String email,username,password,phone_no;

    public UserModel(int user_id, String username, String email, String phone_no, String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.phone_no = phone_no;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
