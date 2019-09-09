package com.sourcey.materiallogindemo.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Offer {
    String notificationTyp;

    public String getNotificationTyp() {
        return notificationTyp;
    }

    public void setNotificationTyp(String notificationTyp) {
        this.notificationTyp = notificationTyp;
    }

    String type;
    String price;
    String city;
    String buildingTyp;

    public String getBuildingTyp() {
        return buildingTyp;
    }

    public void setBuildingTyp(String buildingTyp) {
        this.buildingTyp = buildingTyp;
    }

    String street;
    Double longtuide,lituide;
    Double flon,flit,slon,slit,tlon,tlit,ftlon,ftlit;
    String userName;
    String offerID;

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String UID;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getFlon() {
        return flon;
    }

    public void setFlon(double flon) {
        this.flon = flon;
    }

    public double getFlit() {
        return flit;
    }

    public void setFlit(double flit) {
        this.flit = flit;
    }

    public double getSlon() {
        return slon;
    }

    public void setSlon(double slon) {
        this.slon = slon;
    }

    public double getSlit() {
        return slit;
    }

    public void setSlit(double slit) {
        this.slit = slit;
    }

    public double getTlon() {
        return tlon;
    }

    public void setTlon(double tlon) {
        this.tlon = tlon;
    }

    public double getTlit() {
        return tlit;
    }

    public void setTlit(double tlit) {
        this.tlit = tlit;
    }

    public double getFtlon() {
        return ftlon;
    }

    public void setFtlon(double ftlon) {
        this.ftlon = ftlon;
    }

    public double getFtlit() {
        return ftlit;
    }

    public void setFtlit(double ftlit) {
        this.ftlit = ftlit;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
