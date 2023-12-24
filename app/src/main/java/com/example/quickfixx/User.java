package com.example.quickfixx;

public class User {
    private String name;
    private String address;
    private String city;
    private String state;
    private String phoneNo;
    private String emailId;
    private String password;

    // Constructor for registration
    public User(String name, String address, String city, String state, String phoneNo, String emailId, String password) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.password = password;
    }

    // Constructor for login
    public User(String identifier, String password) {
        this.emailId = identifier;
        this.phoneNo = identifier;
        this.password = password;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
