package com.sourcey.materiallogindemo.model;

public class LinkOffer {
    String offerID,userID;

    public LinkOffer() {
    }

    public LinkOffer(String offerID, String userID) {
        this.offerID = offerID;
        this.userID = userID;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
