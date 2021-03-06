package com.sourcey.materiallogindemo.Model;

public class Chat {
    private String sender,reciver,message,id;
    private Offer offerNeed;

    public Offer getOfferNeed() {
        return offerNeed;
    }

    public void setOfferNeed(Offer offerNeed) {
        this.offerNeed = offerNeed;
    }

    private boolean isseen,isblock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsblock(boolean isblock) {
        this.isblock = isblock;
    }

    public boolean isIsblock() {
        return isblock;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public Chat(String sender, String reciver, String message) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
