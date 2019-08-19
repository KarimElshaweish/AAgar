package com.sourcey.materiallogindemo.Model;

import java.util.List;

public class OfferResult {
    String spinnerType;
    String price;
    String description;
    double longtuide,lituide;
    String city;
    String street;
    String uID;
    String toID;
    String offerID;
    List<String>imageList;


    public String getSpinnerType() {
        return spinnerType;
    }

    public void setSpinnerType(String spinnerType) {
        this.spinnerType = spinnerType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongtuide() {
        return longtuide;
    }

    public void setLongtuide(double longtuide) {
        this.longtuide = longtuide;
    }

    public double getLituide() {
        return lituide;
    }

    public void setLituide(double lituide) {
        this.lituide = lituide;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
