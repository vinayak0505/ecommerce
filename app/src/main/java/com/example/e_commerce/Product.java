package com.example.e_commerce;

public class Product {
    private String userName;
    private String name;
    private String description;
    private String photoUrl;
    private String prise;
    private String boughtBy;

    public Product(){}

    public Product(String userName, String name, String description, String photoUrl, String prise, String boughtBy) {
        this.userName = userName;
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.prise = prise;
        this.boughtBy = boughtBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPrise() {
        return prise;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public String getBoughtBy() {
        return boughtBy;
    }

    public void setBoughtBy(String boughtBy) {
        this.boughtBy = boughtBy;
    }
}
