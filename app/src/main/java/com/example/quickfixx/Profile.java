package com.example.quickfixx;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {
    private String companyName;
    private String service;
    private String whatsappNumber;
    private String experience;
    private String address;
    private List<String> images;

    private String phoneNo;

    public Profile(String companyName, String service, String whatsappNumber, String experience, String address, List<String> images, String phoneNo) {
        this.companyName = companyName;
        this.service = service;
        this.whatsappNumber = whatsappNumber;
        this.experience = experience;
        this.address = address;
        this.images = images;
        this.phoneNo = phoneNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
