package com.sourcey.materiallogindemo.Model;

import java.util.List;

public class OfferResult {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String spinnerType;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    String buildingType;
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
