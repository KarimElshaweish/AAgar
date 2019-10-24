package com.sourcey.materiallogindemo.Model;

public class ChatOfferItem {
    String user,offerUser,offerID;

    public ChatOfferItem(String user, String offerUser, String offerID) {
        this.user = user;
        this.offerUser = offerUser;
        this.offerID = offerID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOfferUser() {
        return offerUser;
    }

    public void setOfferUser(String offerUser) {
        this.offerUser = offerUser;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }
}
