package com.example.quickfixx;

public class Review {

    private String name;
    private String userEmail;
    private String serviceProviderContact;
    private int rating;
    private String review;
    private String date;

    public Review(String name, String userEmail, String serviceProviderContact, int rating, String review, String date) {
        this.name = name;
        this.userEmail = userEmail;
        this.serviceProviderContact = serviceProviderContact;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String userEmail) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getServiceProviderContact() {
        return serviceProviderContact;
    }

    public void setServiceProviderContact(String serviceProviderContact) {
        this.serviceProviderContact = serviceProviderContact;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
