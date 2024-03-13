package com.example.mylogin.utilclasses;

public class User {
    private String name, address, email, password, phone;
    public User() {
        this.name = null;
        this.address = null;
        this.email = null;
        this.password = null;
        this.phone = null;
    }

    public User(String name, String address, String email, String password, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    private void setName(String name) { this.name = name; }
    private void setAddress(String address) { this.address = address; }
    private void setEmail(String email) { this.email = email; }
    private void setPassword(String password) { this.password = password; }
    private void setPhone(String phone) { this.phone = phone; }

    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public String getPhone() { return this.phone; }
}
